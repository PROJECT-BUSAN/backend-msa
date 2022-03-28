package project.investmentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.investmentservice.enums.ChannelServiceReturnType;
import project.investmentservice.enums.ChannelState;
import project.investmentservice.utils.HttpApiController;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.repository.ChannelRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static project.investmentservice.enums.ChannelServiceReturnType.*;
import static project.investmentservice.enums.ChannelState.*;
import static project.investmentservice.enums.UserReadyType.CANCEL;
import static project.investmentservice.enums.UserReadyType.READY;

@RequiredArgsConstructor
@Service
public class ChannelService {

    private final HttpApiController httpApiController;
    private final ChannelRepository channelRepository;
    private Long channelNum = 1L;

    /**
     * 비즈니스 로직
     * Channel 추가
     */
    public Channel createChannel(String channelName, int LimitOfParticipants, double entryFee,
                                 Long hostUserId, String hostname, HashSet<Long> companyIds) {

        //채널 객체 생성
        Channel channel = Channel.create(channelName, channelNum++, LimitOfParticipants, entryFee, hostUserId, hostname, companyIds);

        //채널 저장
        channelRepository.createChannel(channel);
        return channel;
    }

    /**
     * 비즈니스 로직
     * Channel 삭제
     * Channel은 방장이 나갈때 닫을 수있음.
     */
    public void deleteChannel(Channel channel) {
        channelRepository.deleteChannel(channel);
    }

    /**
     * 비즈니스 로직
     * ChannelOne 찾기
     */
    public Channel findOneChannel(String channelId) {
        Channel findChannel = channelRepository.findChannelById(channelId);
        return findChannel;
    }

    /**
     * 비즈니스 로직
     * ChannelAll 찾기
     */
    public List<Channel> findAllChannel() {

        return channelRepository.findAllChannel();
    }


    /**
     * 비즈니스 로직
     * Channel 입장
     */
    public ChannelServiceReturnType enterChannel(String channelId, Long userId, String username) {
        //user_id로 user의 현재 point 정보를 불러오는 로직 필요. 이값을 여기서는 이값을 매개변수로 임시로 선언
        Channel findChannel = findOneChannel(channelId);

        if (findChannel.getChannelState() == STARTED) {
            throw new RuntimeException("이미 실행한 방에 입장하려는 경우 에러");
        }

        List<Long> allUsers = findChannel.getAllUsers();
        double userPoint = 1000000.0;

//        String profileServiceUrl = "http://profile-service:8080/api/v1/profile/";
//        String profileServiceUrl = "http://172.30.1.11:8081/api/v1/profile/";

        /**
         * 만약 입장료보다 point 를 적게 가지고 있을 시 참여 불가능.
         */
//        profileServiceUrl += (userId + "/point");
//        
//        ResponseEntity<String> response = httpApiController.getRequest(profileServiceUrl);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            JsonNode node = mapper.readTree(response.getBody());
//            userPoint = Double.parseDouble(node.get("userPoint").asText());
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        if (userPoint < findChannel.getEntryFee()) {
            // 포인트 부족
            return POINTLACK;
        }
        if(findChannel.getLimitOfParticipants() <= findChannel.getUsers().size()) {
            // 인원 가득참.
            return FULLCHANNEL;
        } else {
            // channel 제한 인원이 현재 입장된 유저의 수보다 클 때 입장 가능
            // userPoint 는 게임이 시작할때 차감한다.
            // 채널에 유저 입장.
            findChannel.getUsers().put(userId, new User(userPoint, username));
            channelRepository.updateChannel(findChannel);
            return SUCCESS;
        }
//        else if (findChannel.getUsers().get(userId) != null) {
//            // 이미 방에 참여한 상태
//            return ALREADYIN;
//        }
    }

    @Data
    @AllArgsConstructor
    static class PointDTO {
        double userPoint;
    }


    /**
     * 비즈니스 로직
     * Channel 퇴장
     */
    public Channel exitChannel(String channelId, Long userId) {
        Channel findChannel = findOneChannel(channelId);
        findChannel.getUsers().remove(userId);
        channelRepository.updateChannel(findChannel);
        return findChannel;
    }

    public void updateChannel(Channel channel) {
        channelRepository.updateChannel(channel);
    }

    /**
     * 비즈니스 로직
     * Channel READY Set
     */
    public Channel setReady(String channelId, Long userId) {
        Channel findChannel = findOneChannel(channelId);
        System.out.println("findChannel.User = " + findChannel.getUsers().get(userId));
        findChannel.getUsers().get(userId).setReadyType(READY);
        channelRepository.updateChannel(findChannel);
        return findChannel;
    }

    /**
     * 비즈니스 로직
     * Channel READY CANCEL
     */
    public Channel cancelReady(String channelId, Long userId) {
        Channel findChannel = findOneChannel(channelId);
        findChannel.getUsers().get(userId).setReadyType(CANCEL);
        channelRepository.updateChannel(findChannel);
        return findChannel;
    }

    /**
     * 비즈니스 로직
     * Check Channel ready state
     */
    public boolean checkReadyState(String channelId) {
        Channel findChannel = findOneChannel(channelId);
        for(Long Key : findChannel.getUsers().keySet()) {
            if(Key == findChannel.getHostId()) continue;
            if(findChannel.getUsers().get(Key).getReadyType() == CANCEL) return false;
        }
        return true;
    }


    /**
     *    게임이 종료되면 모든 유저가 가지고 있는 주식이 종가에 매도된다.
     */
    public void sellAllStock(Map<Long, Double> closeValue, Channel nowChannel) {
        Map<Long, User> users = nowChannel.getUsers();
        System.out.println("users.size() = " + users.size());

        for(Long userKey : users.keySet()) {
            User user = users.get(userKey);
            System.out.println("user.getName() = " + user.getName());
            double userSeedMoney = user.getSeedMoney();
            if (user.getCompanies().isEmpty()) {
                continue;
            }
            for(Long companyKey : user.getCompanies().keySet()) {
                UsersStock usersStock = user.getCompanies().get(companyKey);
                if(usersStock.getQuantity() == 0) continue;
                double sellPrice = closeValue.get(companyKey);
                System.out.println("sellPrice = " + sellPrice);
                userSeedMoney += (sellPrice * usersStock.getQuantity());
                System.out.println("userSeedMoney = " + userSeedMoney);
            }
            user.setSeedMoney(userSeedMoney);
        }
        channelRepository.updateChannel(nowChannel);
    }

    /**
     * 특정 channel의 특정 User 반환
     */
    public User findUserById(String channelId, Long userId) {
        Channel channel = channelRepository.findChannelById(channelId);
        return channel.findUserById(userId);
    }
}
