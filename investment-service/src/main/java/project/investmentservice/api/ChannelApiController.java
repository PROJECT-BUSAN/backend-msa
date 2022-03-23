package project.investmentservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.*;
import project.investmentservice.dto.channel.*;
import project.investmentservice.enums.ChannelServiceReturnType;
import project.investmentservice.enums.HttpReturnType;
import project.investmentservice.service.AuthenticateService;
import project.investmentservice.service.ChannelService;
import project.investmentservice.utils.HttpApiController;

import javax.validation.Valid;
import java.util.*;

import static project.investmentservice.enums.ChannelServiceReturnType.*;
import static project.investmentservice.enums.HttpReturnType.FAIL;

/**
 *  채널 삭제는 소켓 message로 처리
 *  1) 방장이 EXIT를 메시지를 보내는경우 -> CANCEL 메시지를 모든 User에게 보냄
 *  2) 방장이 아닌 User가 EXIT 메시지를 보내는경우 -> 채널 정보를 갱신하여 모든 User에게 다시 전송
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/investment")
public class ChannelApiController {

    private final ChannelService channelService;
    private final AuthenticateService authService;
    private final HttpApiController httpApiController;
    
    /**
     * 모든 채널들의 정보 반환
     * 대기중인 방 리스트를 띄울 때 필요함
     * 
     * @return 
     * String id;
     * Long channelNum;
     * String channelName;
     * int LimitOfParticipants;
     * double entryFee;    -> 참가비
     * Map<Long, User> users = new HashMap<>();
     * double pointPsum;
     * Long hostId;
     * String hostName;
     */
    @GetMapping("/channel")
    public AllChannelResponse channels() {
        List<Channel> channels = channelService.findAllChannel();
        return new AllChannelResponse(channels);
    }

    /**
     * 채널 생성 api
     * @param request
     * @NotNull String name; -> 방 이름 필수
     * @NotNull int limitOfParticipants; -> 방 제한인원 필수
     * @NotNull Long entryFee; -> 참가비 필수
     * Long userId; -> 방장의 id
     * String username; -> 방장의 username
     * 
     * @return{
     *      id, channelName, channelNum
     * }
     */
    @PostMapping("/channel")
    public CreateChannelResponse createChannel(@RequestBody @Valid CreateChannelRequest request) {
        Channel channel = channelService.createChannel(request.getName(), request.getLimitOfParticipants(), request.getEntryFee(), request.getUserId(), request.getUsername());
        return new CreateChannelResponse(channel.getId(), channel.getChannelNum(), channel.getChannelName());
    }

    
    /**
     * 채널 입장
     * 채널 소켓 연결 전에 호출해서 유저가 방에 입장할 수 있는지 확인한다. (유저의 포인트 >= 참가비)
     * 확인되면 
     * @param channelId -> uuid
     * @param request
     * 
     * @return
     */
    @PostMapping("/channel/{channelId}")
    public EnterChannelResponse enterChannel(@PathVariable("channelId") String channelId, @RequestBody @Valid EnterChannelRequest request) {
        Long userId = request.getUserId();
        String username = request.getUsername();
        authService.LoginCheck(userId, username);
        
        ChannelServiceReturnType result = channelService.enterChannel(channelId, userId, username);
        if(result == ChannelServiceReturnType.SUCCESS) {
            return new EnterChannelResponse(HttpReturnType.SUCCESS, "채널에 입장합니다.", userId, username);
        }
        else if(result == POINTLACK) {
            return new EnterChannelResponse(FAIL, "채널에 입장하기 위한 포인트가 부족합니다.", userId, username);
        }
        else if(result == FULLCHANNEL) {
            return new EnterChannelResponse(FAIL, "채널에 인원이 가득찼습니다.", userId, username);
        }
        else {
            return new EnterChannelResponse(FAIL, "Server Error 500.", userId, username);
        }
    }
}
