package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Channel;
import project.investmentservice.repository.ChannelRedisRepository;

@Service
@RequiredArgsConstructor
public class ChannelService {

    @Autowired
    private ChannelRedisRepository channelRedisRepository;
    private Long channelNum = 1L;

    /**
     * 비즈니스 로직
     * Channel 추가
     */
    public Channel createChannel(Long managerId, int numOfParticipants, Long entryFee) {
        //채널 객체 생성
        Channel channel = new Channel(channelNum++, numOfParticipants, entryFee, managerId);

        //채널 저장
        channelRedisRepository.save(channel);
        return channel;
    }

    public void deleteChannel(Long channelId) {
        //채널 찾기
        Channel findChannel = findOneChannel(channelId);

        //채널 삭제
        channelRedisRepository.delete(findChannel);
    }

    /**
     * 비즈니스 로직
     * ChannelOne 찾기
     */
    public Channel findOneChannel(Long channelId) {
        Channel findChannel = channelRedisRepository.findById(channelId).get();
        return findChannel;
    }

    /**
     * 비즈니스 로직
     * ChannelAll 찾기
     */
    public Iterable<Channel> findAllChannel() {
        return channelRedisRepository.findAll();
    }


    /**
     * 비즈니스 로직
     * Channel 입장
     */
    public void participantChannel(Long channelId, Long userId, Long userPoint) {
        //user_id로 user의 현재 point 정보를 불러오는 로직 필요. 이값을 여기서는 이값을 매개변수로 임시로 선언

        Channel findChannel = findOneChannel(channelId);
        if(findChannel.getNumOfParticipants() > findChannel.getUsers().size()) {
            //channel을 생성할때의 제한 인원이 현재 channel에 있는 인원보다 클때 -> channel 입장 비용 확인
            if(findChannel.getEntryFee() <= userPoint) {
                //userPoint는 게임이 시작할때 차감하는걸로
                System.out.println("findChannel = " + findChannel.getId());
                findChannel.addUser(userId);
                channelRedisRepository.save(findChannel);
            }
        }
    }

    /**
     * 비즈니스 로직
     * Channel 퇴장
     */
    public void exitChannel(Long channelId, Long userId) {
        Channel findChannel = findOneChannel(channelId);
        findChannel.removeUser(userId);
        channelRedisRepository.save(findChannel);
    }

}
