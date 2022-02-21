package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Channel;
import project.investmentservice.repository.ChannelRepository;

@Service
@RequiredArgsConstructor
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;
    private Long channelNum = 1L;

    /**
     * 비즈니스 로직
     * Channel 추가
     */
    public Channel createChannel(String channelName, int LimitOfParticipants, Long entryFee, Long hostId) {
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
        //채널 찾기
        Channel findChannel = findOneChannel(channelId);

        //채널 삭제
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
    public Iterable<Channel> findAllChannel() {
        return channelRepository.findAllChannel();
    }


    /**
     * 비즈니스 로직
     * Channel 입장
     */
    public void participantChannel(String channelId, Long userId, Long userPoint) {
        //user_id로 user의 현재 point 정보를 불러오는 로직 필요. 이값을 여기서는 이값을 매개변수로 임시로 선언

        Channel findChannel = findOneChannel(channelId);
        if(findChannel.getLimitOfParticipants() > findChannel.getUsers().size()) {
            //channel을 생성할때의 제한 인원이 현재 channel에 있는 인원보다 클때 -> channel 입장 비용 확인
            if(findChannel.getEntryFee() <= userPoint) {
                //userPoint는 게임이 시작할때 차감하는걸로
                findChannel.addUser(userId);
                channelRepository.enterChannel(channelId);
            }
        }
    }

    /**
     * 비즈니스 로직
     * Channel 퇴장
     */
    public void exitChannel(String channelId, Long userId) {
        channelRepository.exitChannel(channelId, userId);
    }


}
