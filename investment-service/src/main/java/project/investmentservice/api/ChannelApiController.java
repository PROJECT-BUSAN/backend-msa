package project.investmentservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.StockInfo;
import project.investmentservice.domain.dto.ServerMessage;
import project.investmentservice.domain.dto.StockInfoMessage;
import project.investmentservice.pubsub.RedisPublisher;
import project.investmentservice.repository.ChannelRepository;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.CompanyService;
import project.investmentservice.service.StockInfoService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static project.investmentservice.api.ChannelApiController.EnterChannelResponse.returnType.FAIL;
import static project.investmentservice.api.ChannelApiController.EnterChannelResponse.returnType.SUCCESS;
import static project.investmentservice.domain.dto.ClientMessage.MessageType.ENTER;
import static project.investmentservice.domain.dto.ServerMessage.MessageType.RENEWAL;

/**
 *  채널 삭제는 소켓 message로 처리
 *  1) 방장이 EXIT를 메시지를 보내는경우 -> CANCEL 메시지를 모든 User에게 보냄
 *  2) 방장이 아닌 User가 EXIT 메시지를 보내는경우 -> 채널 정보를 갱신하여 모든 User에게 다시 전송
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/investment")
public class ChannelApiController {

    @Autowired
    private ChannelService channelService;
    private final RedisPublisher redisPublisher;
    private final ChannelRepository channelRepository;
    private final StockInfoService stockInfoService;
    private final CompanyService companyService;

    //모든 채널 반환
    @GetMapping("/channel")
    public AllChannelResponse channels() {
        List<Channel> channels = channelService.findAllChannel();
        return new AllChannelResponse(channels);
    }

    //채널 생성
    @PostMapping("/channel")
    public CreateChannelResponse createChannel(@RequestBody @Valid CreateChannelRequest request) {
        Channel channel = channelService.createChannel(request.getName(), request.getLimitOfParticipants(), request.getEntryFee(), request.getUserId());
        return new CreateChannelResponse(channel.getId(), channel.getChannelNum(), channel.getChannelName());
    }

    // 채널 입장
    @PostMapping("/channel/enter/{channelId}")
    public EnterChannelResponse enterChannel(@PathVariable("channelId") String channelId, @RequestBody @Valid EnterChannelRequest request) {
        int result = channelService.enterChannel(channelId, request.getUser_id());
        if(result == 0) {
            return new EnterChannelResponse(SUCCESS, "채널에 입장합니다.");
        }
        else if(result == 1) {
            return new EnterChannelResponse(FAIL, "채널에 입장하기 위한 포인트가 부족합니다.");

        }
        else if(result == 2) {
            return new EnterChannelResponse(FAIL, "채널에 인원이 가득찼습니다.");
        }
        else {
            return new EnterChannelResponse(FAIL, "Server Error 500.");
        }
    }

    //게임 시작
    @PostMapping("/channel/start/{channelId}")
    public void startChannel(@PathVariable("channelId") String channelId) {
        Channel channel = channelService.findOneChannel(channelId);
        
        // 채널의 유저가 모두 ready 상태인지 확인
        // ...

        HashSet<Long> companyIds = companyService.selectInGameCompany(2);
        
        for(Long cid: companyIds){
            stockInfoService.periodStockInfo(cid);
        }
        
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int idx = 0;
            @Override
            public void run() {
                if(idx < 60) {
                    StockInfoMessage stockInfoMessage = new StockInfoMessage(RENEWAL, channel.getId(), channel.getUsers(), null);
                    redisPublisher.publish(channelRepository.getTopic(channelId), serverMessage);
                    System.out.println(" = publish" + channelId);
                }
                else {
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 10000);
    }

    @Data
    @AllArgsConstructor
    public static class AllChannelResponse {
        private List<Channel> channels;
    }

    @Data
    @AllArgsConstructor
    public static class CreateChannelRequest {
        @NotNull
        private String name;
        @NotNull
        private int LimitOfParticipants;
        @NotNull
        private Long entryFee;
        private Long userId;

        public CreateChannelRequest() {
        }
    }

    @Data
    @AllArgsConstructor
    public static class CreateChannelResponse {
        private String id;
        private Long channelNum;
        private String channelName;
    }

    @Data
    public static class EnterChannelRequest {
        @NotNull
        private Long user_id;
    }

    @Data
    @AllArgsConstructor
    public static class EnterChannelResponse {
        public enum returnType {
            SUCCESS, FAIL
        }
        private returnType type;
        private String message;
    }
}
