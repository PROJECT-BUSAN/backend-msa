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

import static project.investmentservice.api.ChannelApiController.EnterChannelResponse.returnType.FAIL;
import static project.investmentservice.api.ChannelApiController.EnterChannelResponse.returnType.SUCCESS;

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
    private final HttpApiController httpApiController;

    @PostMapping("/testpoint")
    public ResponseEntity test() {
        String profileServiceUrl = "http://172.30.1.11:8081/api/v1/profile/";
        double userPoint = -1.0;
        /**
         * 입장할 때 유저 프로필의 point를 차감한다.
         * 만약 입장료보다 point를 적게 가지고 있을 시 참여 불가능.
         */
        profileServiceUrl += ("2" + "/point");

        ResponseEntity<String> response = httpApiController.getRequest(profileServiceUrl);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(response.getBody());
            System.out.println("node = " + node);
            userPoint = Double.parseDouble(node.get("userPoint").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (userPoint < 500) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.OK);

    }

    //모든 채널 반환
    @GetMapping("/channel")
    public AllChannelResponse channels() {
        List<Channel> channels = channelService.findAllChannel();
        return new AllChannelResponse(channels);
    }

    //채널 생성
    @PostMapping("/channel")
    public CreateChannelResponse createChannel(@RequestBody @Valid CreateChannelRequest request) {

        Channel channel = channelService.createChannel(request.getName(), request.getLimitOfParticipants(), request.getEntryFee(), request.getUserId(), request.getUsername());
        return new CreateChannelResponse(channel.getId(), channel.getChannelNum(), channel.getChannelName());
    }

    // 채널 입장
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
    @AllArgsConstructor
    public static class CreateChannelRequest {
        @NotNull
        private String name;
        @NotNull
        private int limitOfParticipants;
        @NotNull
        private Long entryFee;
        private Long userId;
        private String username;

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
        private Long userId;
        private String username;
    }

    @Data
    @AllArgsConstructor
    public static class EnterChannelResponse {
        public enum returnType {
            SUCCESS, FAIL
        }
        private returnType type;
        private String message;
        private Long userId;
        private String userName;
    }

    
}
