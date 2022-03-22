package project.investmentservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import project.investmentservice.utils.HttpApiController;
import project.investmentservice.domain.*;
import project.investmentservice.domain.dto.*;
import project.investmentservice.domain.dto.ClientMessage.MessageType;
import project.investmentservice.pubsub.RedisPublisher;
import project.investmentservice.repository.ChannelRepository;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.CompanyService;
import project.investmentservice.service.StockInfoService;

import java.util.*;

import static project.investmentservice.domain.dto.ServerMessage.MessageType.*;
import static project.investmentservice.domain.dto.ClientMessage.MessageType.*;

@RequiredArgsConstructor
@Controller
public class ChannelMessageController {

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;
    private final RedisPublisher redisPublisher;
    private final CompanyService companyService;
    private final StockInfoService stockInfoService;
    private final HttpApiController httpApiController;

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
        MessageType messageType = clientMessage.getType();

        // 채널 ENTER TYPE

        if (ENTER.equals(messageType)) {
            // 채널 입장 성공
            Channel channel = channelService.findOneChannel(clientMessage.getChannelId());
            ServerMessage serverMessage = new ServerMessage(RENEWAL, channel.getId(), channel.getUsers(), null);
            redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
        }
        // 채널 EXIT TYPE
        else if(EXIT.equals(messageType)) {
            Channel exitChannel = channelRepository.findChannelById(clientMessage.getChannelId());
            String exitChannelId = exitChannel.getId();
            // Host가 퇴장하는 경우 -> 채널 삭제
            if(exitChannel.getHostId() ==  clientMessage.getSenderId()) {
                ServerMessage serverMessage = new ServerMessage(CLOSE, exitChannelId, null, "채널이 닫혔습니다.");
                redisPublisher.publish(channelRepository.getTopic(exitChannelId), serverMessage);
                channelService.deleteChannel(exitChannel);
            }
            // Host가 아닌 사용자가 퇴장하는 경우 -> 채널 유지
            else {
                Channel channel = channelService.exitChannel(exitChannelId, clientMessage.getSenderId());
                ServerMessage serverMessage = new ServerMessage(RENEWAL, exitChannelId, channel.getUsers(), null);
                redisPublisher.publish(channelRepository.getTopic(exitChannelId), serverMessage);
            }
        }

        // 채널 READY TYPE
        else if(READY.equals(messageType)) {
            String channelId = clientMessage.getChannelId();
            Channel channel = channelService.setReady(channelId, clientMessage.getSenderId());
            if(channel.getHostId() == clientMessage.getSenderId()) {
                //모든인원 ready 상태 확인
                if(channelService.checkReadyState(channelId)) {
                    gameStart(channelId);
                }
                else {
                    ServerMessage serverMessage = new ServerMessage(NOTICE, channelId, channel.getUsers(), "모든 참가자가 준비를 완료해야 합니다.");
                    redisPublisher.publish(channelRepository.getTopic(channelId), serverMessage);
                }
            }
            else {
                ServerMessage serverMessage = new ServerMessage(RENEWAL, channelId, channel.getUsers(), null);
                redisPublisher.publish(channelRepository.getTopic(channelId), serverMessage);
            }
        }
        // 채널 CANCEL TYPE (준비 취소)
        else if(CANCEL.equals(messageType)) {
            Channel channel = channelService.cancelReady(clientMessage.getChannelId(), clientMessage.getSenderId());
            ServerMessage serverMessage = new ServerMessage(RENEWAL, clientMessage.getChannelId(), channel.getUsers(), null);
            redisPublisher.publish(channelRepository.getTopic(clientMessage.getChannelId()), serverMessage);
        }
    }
    
    
    @MessageMapping("/game/start")
    public void gameStart(String channelId) {
        Channel nowChannel = channelService.findOneChannel(channelId);
        List<Long> allUsers = nowChannel.getAllUsers();
        
//        String profileServiceUrl = "http://profile-service:8080/api/v1/profile/point/bulk";
//        
//        AllUserPointDeduction deduction = new AllUserPointDeduction(
//                allUsers,
//                nowChannel.getEntryFee()
//        );
//        ResponseEntity<String> response = httpApiController.postRequest(profileServiceUrl, deduction);
//        if (!response.getStatusCode().equals(HttpStatus.OK)) {
//            ErrorMessage errorMessage = new ErrorMessage("");
//            
//            return;
//        }

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
                        System.out.println("stockInfo.getClose() = " + stockInfo.getClose());
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
        timer.schedule(timerTask, 0, 1000);

//         타이머가 종료된 이후 다음 로직을 수행해야 하므로
//         lock이 시그널을 받을 때까지 기다린다.
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
            }
        }
        
        
        
        
        // 게임에 참여한 유저가 가지고 있는 주식 전체 강제 매도
        channelService.sellAllStock(closeValue, nowChannel);
        
        
        // 게임 진행에 사용된 기업의 이름, 시작날짜, 종료 날짜를 반환한다.
        int k = 0;
        List<StockResult> stockResults = new ArrayList<>();
        for(Long cid: companyIds){
            Company company = companyService.findOneCompany(cid);
            StockResult stockResult = new StockResult(
                    cid,
                    company.getStock_name(),
                    stockLists.get(k).get(0).getDate(),
                    stockLists.get(k).get(59).getDate(),
                    "게임이 종료됩니다."
            );
            stockResults.add(stockResult);
            k++;
        }

        StockGameEndMessage stockGameEndMessage = new StockGameEndMessage(
                stockResults, 
                nowChannel.gameResult(), 
                "CLOSE"
        );
        redisPublisher.publishEndMessage(
                channelRepository.getTopic(channelId), 
                stockGameEndMessage
        );

        channelService.deleteChannel(nowChannel);
    }

    @Data
    @AllArgsConstructor
    static class AllUserPointDeduction{
        List<Long> userIds;
        double fee;
    } 
}
