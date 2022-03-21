package project.investmentservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.*;
import project.investmentservice.domain.dto.ReturnType;
import project.investmentservice.domain.dto.StockGameEndMessage;
import project.investmentservice.domain.dto.StockInfoMessage;
import project.investmentservice.domain.dto.StockResult;
import project.investmentservice.pubsub.RedisPublisher;
import project.investmentservice.repository.ChannelRepository;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.CompanyService;
import project.investmentservice.service.InvestmentService;
import project.investmentservice.service.StockInfoService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

import static project.investmentservice.domain.dto.ReturnType.FAIL;
import static project.investmentservice.domain.dto.ReturnType.SUCCESS;

/**
 *  채널 삭제는 소켓 message로 처리
 *  1) 방장이 EXIT를 메시지를 보내는경우 -> CANCEL 메시지를 모든 User에게 보냄
 *  2) 방장이 아닌 User가 EXIT 메시지를 보내는경우 -> 채널 정보를 갱신하여 모든 User에게 다시 전송
 */

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/investment/channel")
public class ChannelApiController {

    private final ChannelService channelService;
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
        int result = channelService.enterChannel(channelId, userId, username);
        if(result == 0) {
            return new EnterChannelResponse(SUCCESS, "채널에 입장합니다.", userId, username);
        }
        else if(result == 1) {
            return new EnterChannelResponse(FAIL, "채널에 입장하기 위한 포인트가 부족합니다.", userId, username);

        }
        else if(result == 2) {
            return new EnterChannelResponse(FAIL, "채널에 인원이 가득찼습니다.", userId, username);
        }
        else {
            return new EnterChannelResponse(FAIL, "Server Error 500.", userId, username);
        }
    }

    @Data
    @AllArgsConstructor
    public static class AllChannelResponse {
        private List<Channel> channels;
    }

    @Data
    public static class CreateChannelRequest {
        @NotNull
        private String name;
        @NotNull
        private int limitOfParticipants;
        @NotNull
        private Long entryFee;
        private Long userId;
        private String username;
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
        private Long userId;
        private String username;
    }

    @Data
    @AllArgsConstructor
    public static class EnterChannelResponse {
        private ReturnType type;
        private String message;
        private Long userId;
        private String userName;
    }

    
}
