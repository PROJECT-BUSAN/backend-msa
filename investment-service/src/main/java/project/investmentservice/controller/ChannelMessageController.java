package project.investmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.dto.ClientMessage;
import project.investmentservice.domain.dto.ServerMessage;
import project.investmentservice.pubsub.RedisPublisher;
import project.investmentservice.repository.ChannelRepository;
import project.investmentservice.service.ChannelService;

import static project.investmentservice.domain.dto.ServerMessage.MessageType.*;
import static project.investmentservice.domain.dto.ClientMessage.MessageType.*;

@RequiredArgsConstructor
@Controller
public class ChannelMessageController {

    private final RedisPublisher redisPublisher;
    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    /**
     * websocket "/pub/game/message"로 들어오는 메시징을 처리한다.
     */

    /**
     * 클라이언트에서 channel에 진입할때 메시지와 channel의 전체 상태에 관한 메시지 2개가 필요하다.
     * 진입할때 -> channel의 전체 상태를 변경시키면? 메시지는 클라이언트로부터 받고,
     * 전체 channel 상태에 이 값을 갱신하고 계속 publish하면 될듯
     * 근데 channel 상태에 관한 메시지는 어디서 관리하냐? 가 문제임 -> redis에 저장된 channel에 저장하자.
     * ChannelMessage가 들어오면 channel 정보를 message로 만들어 뿌려주자
     */
    @MessageMapping("/game/message")
    public void message(ClientMessage clientMessage) {
        // 채널 ENTER TYPE
        if (ENTER.equals(clientMessage.getType())) {
            boolean EnterResult = channelService.enterChannel(clientMessage.getChannelId(), clientMessage.getSenderId());
            // 채널 입장 성공
            if(EnterResult == true) {
                Channel channel = channelService.findOneChannel(clientMessage.getChannelId());
                ServerMessage serverMessage = new ServerMessage(RENEWAL, channel.getId(), channel.getUsers(), null);
                redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
            }
            // 채널 입장 실패
            else {
                return;
            }
        }
        // 채널 EXIT TYPE
        else if(EXIT.equals(clientMessage.getType())) {
            Channel exitChannel = channelRepository.findChannelById(clientMessage.getChannelId());
            // Host가 퇴장하는 경우 -> 채널 삭제
            if(exitChannel.getHostId() ==  clientMessage.getSenderId()) {
                ServerMessage serverMessage = new ServerMessage(CLOSE, clientMessage.getChannelId(), null, "채널이 닫혔습니다.");
                redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
                channelService.deleteChannel(clientMessage.getChannelId());
            }
            // Host가 아닌 사용자가 퇴장하는 경우 -> 채널 유지
            else {
                Channel channel = channelService.exitChannel(clientMessage.getChannelId(), clientMessage.getSenderId());
                ServerMessage serverMessage = new ServerMessage(RENEWAL, clientMessage.getChannelId(), channel.getUsers(), null);
                redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
            }
        }
        // 채널 READY TYPE
        else if(READY.equals(clientMessage.getType())) {
            Channel channel = channelService.setReady(clientMessage.getChannelId(), clientMessage.getSenderId());
            if(channel.getHostId() == clientMessage.getSenderId()) {
                //모든인원 ready 상태 확인
                if(channelService.checkReadyState(clientMessage.getChannelId())) {
                    ServerMessage serverMessage = new ServerMessage(START, clientMessage.getChannelId(), channel.getUsers(), "게임화면으로 넘어갑니다.");
                    redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);

                }
                else {
                    ServerMessage serverMessage = new ServerMessage(NOTICE, clientMessage.getChannelId(), channel.getUsers(), "모든 참가자가 준비를 완료해야 합니다.");
                    redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
                }
            }
            else {
                ServerMessage serverMessage = new ServerMessage(RENEWAL, clientMessage.getChannelId(), channel.getUsers(), null);
                redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
            }
        }
        // 채널 CANCEL TYPE (준비 취소)
        else if(CANCEL.equals(clientMessage.getType())) {
            Channel channel = channelService.cancelReady(clientMessage.getChannelId(), clientMessage.getSenderId());
            ServerMessage serverMessage = new ServerMessage(RENEWAL, clientMessage.getChannelId(), channel.getUsers(), null);
            redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
        }

    }
}
