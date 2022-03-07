package project.investmentservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.*;
import project.investmentservice.domain.dto.StockInfoMessage;
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
@RequestMapping("/api/v1/investment")
public class ChannelApiController {

    private final ChannelService channelService;
    private final RedisPublisher redisPublisher;
    private final ChannelRepository channelRepository;
    private final StockInfoService stockInfoService;
    private final CompanyService companyService;
    private final InvestmentService investmentService;

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

    // 게임 시작 및 종료
    @PostMapping("/channel/start/{channelId}")
    public ResponseEntity startChannel(@PathVariable("channelId") String channelId) {
        
        // 채널의 유저가 모두 ready 상태인지 확인 -> checkReadyState 함수쓰셈 만들어놨다.
        if (!channelService.checkReadyState(channelId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // 게임에서 사용할 기업을 랜덤으로 n개 가져온다.
        HashSet<Long> companyIds = companyService.selectInGameCompany(2);
        
        // 게임에서 사용할 n개의 기업에 대한 주가 정보를 저장하는 배열
        List<List<StockInfo>> stockLists = new ArrayList<>();
        for(Long cid: companyIds){
            stockLists.add(stockInfoService.getPeriodStockInfo(cid));
        }

        // <company_id, closePrice>
        Map<Long, Double> closeValue = new HashMap<>();
        
        // 타이머 종료를 위한 lock 객체 설정
        Object lock = new Object();
        // 10초에 한 번씩 주가 정보를 전송한다.
        // 주요 게임 로직을 담당한다.
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int idx = 0;
            @Override
            public void run() {
                if(idx < 60) {
                    for(int i = 0; i < stockLists.size(); i++){
                        StockInfo stockInfo = stockLists.get(i).get(idx);
                        StockInfoMessage stockInfoMessage = new StockInfoMessage(
                                stockInfo.getDate(),
                                stockInfo.getClose(),
                                stockInfo.getOpen(),
                                stockInfo.getHigh(),
                                stockInfo.getLow(),
                                stockInfo.getVolume(),
                                stockInfo.getCompany().getId()
                        );
                        redisPublisher.publishStock(channelRepository.getTopic(channelId), stockInfoMessage);
                        closeValue.put(stockInfo.getCompany().getId(), stockInfo.getClose());
                    }
                    idx++;
                }
                else {
                    // 타이머가 종료되면 lock에 시그널을 보낸다.
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 10000);
        
        // 타이머가 종료된 이후 다음 로직을 수행해야 하므로
        // lock이 시그널을 받을 때까지 기다린다.
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
            }
        }

        /**
         *    게임이 종료되면 모든 유저가 가지고 있는 주식이 종가에 매도된다.
         */
        Channel nowChannel = channelService.findOneChannel(channelId);
        Map<Long, User> users = nowChannel.getUsers();

        for(Long userKey : users.keySet()) {
            User user = users.get(userKey);
            double userSeedMoney = user.getSeedMoney();
            for(Long companyKey : user.getCompanies().keySet()) {
                UsersStock usersStock = user.getCompanies().get(companyKey);
                if(usersStock.getQuantity() == 0L) continue;
                double sellPrice = closeValue.get(companyKey);
                userSeedMoney += (sellPrice * usersStock.getQuantity());
            }
            user.setSeedMoney(userSeedMoney);
        }
        channelRepository.updateChannel(nowChannel);


        // 게임 진행에 사용된 기업의 이름, 시작날짜, 종료 날짜를 반환한다.
        int k = 0;
        List<gameEndResponse> responseList = new ArrayList<>();
        for(Long cid: companyIds){
            Company company = companyService.findOneCompany(cid);
            gameEndResponse gameEndResponse = new gameEndResponse(
                    cid,
                    company.getStock_name(),
                    stockLists.get(k).get(0).getDate(),
                    stockLists.get(k).get(59).getDate(),
                    "게임이 종료됩니다."
            );
            responseList.add(gameEndResponse);
            k++;
        }
        
        return new ResponseEntity(responseList, HttpStatus.OK);
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

    @Data
    @AllArgsConstructor
    public static class gameEndResponse {
        // 게임에 사용된 기업의 이름, 주식 시작-종료 날짜
        private Long company_id;
        private String stockName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String message;
    }
    
}
