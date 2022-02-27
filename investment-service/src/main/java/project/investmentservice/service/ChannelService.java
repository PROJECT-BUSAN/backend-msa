package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.repository.ChannelRepository;

import java.util.List;

import static project.investmentservice.domain.User.ReadyType.CANCEL;
import static project.investmentservice.domain.User.ReadyType.READY;

@RequiredArgsConstructor
@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;
    private Long channelNum = 1L;

    /**
     * 비즈니스 로직
     * Channel 추가
     */
    public Channel createChannel(String channelName, int LimitOfParticipants, double entryFee, Long hostId) {
        //채널 객체 생성
        Channel channel = Channel.create(channelName, channelNum++, LimitOfParticipants, entryFee, hostId);

        //채널 저장
        channelRepository.createChannel(channel);
        return channel;
    }

    /**
     * 비즈니스 로직
     * Channel 삭제
     * Channel은 방장이 나갈때 닫을 수있음.
     */
    public void deleteChannel(String channelId) {
        Channel findChannel = channelRepository.findChannelById(channelId);
        channelRepository.deleteChannel(findChannel);
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
    public int enterChannel(String channelId, Long userId) {
        //user_id로 user의 현재 point 정보를 불러오는 로직 필요. 이값을 여기서는 이값을 매개변수로 임시로 선언
        double userPoint = 1000.0;

        Channel findChannel = findOneChannel(channelId);
        if(findChannel.getLimitOfParticipants() > findChannel.getUsers().size()) {

            //channel을 생성할때의 제한 인원이 현재 channel에 있는 인원보다 클때 -> channel 입장 비용 확인
            if(findChannel.getEntryFee() <= userPoint) {
                //userPoint는 게임이 시작할때 차감하는걸로
                findChannel.getUsers().put(userId, new User(userPoint));
                channelRepository.updateChannel(findChannel);
                return 0;
            }
            else {
                //포인트 부족
                return 1;
            }
        }
        else {
            // 인원 가득참.
            return 2;
        }
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

    /**
     * 비즈니스 로직
     * Channel READY Set
     */
    public Channel setReady(String channelId, Long userId) {
        Channel findChannel = findOneChannel(channelId);
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

}
