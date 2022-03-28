package project.investmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import project.investmentservice.enums.ChannelState;
import project.investmentservice.enums.ClientMessageType;
import project.investmentservice.enums.ServerMessageType;
import project.investmentservice.service.InvestmentService;
import project.investmentservice.utils.CustomJsonMapper;
import project.investmentservice.utils.HttpApiController;
import project.investmentservice.domain.*;
import project.investmentservice.pubsub.RedisPublisher;
import project.investmentservice.repository.ChannelRepository;
import project.investmentservice.service.ChannelService;
import project.investmentservice.service.CompanyService;
import project.investmentservice.service.StockInfoService;

import java.util.*;

import static project.investmentservice.dto.SocketDto.*;
import static project.investmentservice.enums.ChannelState.STARTED;
import static project.investmentservice.enums.ChannelState.WAITING;
import static project.investmentservice.enums.ServerMessageType.*;

@RequiredArgsConstructor
@Controller
public class ChannelMessageController {

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;
    private final RedisPublisher redisPublisher;
    private final CompanyService companyService;
    private final StockInfoService stockInfoService;
    private final InvestmentService investmentService;
    private final HttpApiController httpApiController;

    /**
     * websocket "/pub/game/message"로 들어오는 메시징을 처리한다.
     */

    /**
     * 클라이언트에서 channel에 진입할때 메시지와 channel의 전체 상태에 관한 메시지 n개가 필요하다.
     * 진입할때 -> channel의 전체 상태를 변경시키면? 메시지는 클라이언트로부터 받고,
     * 전체 channel 상태에 이 값을 갱신하고 계속 publish하면 될듯
     * 근데 channel 상태에 관한 메시지는 어디서 관리하냐? 가 문제임 -> redis에 저장된 channel에 저장하자.
     * ChannelMessage가 들어오면 channel 정보를 message로 만들어 뿌려주자
     */
    @MessageMapping("/game/channel")
    public void channelMessage(ClientMessage clientMessage) {
        ClientMessageType messageType = clientMessage.getType();
        System.out.println("messageType = " + messageType);
        String channelId = clientMessage.getChannelId();
        Long senderId = clientMessage.getSenderId();
        String senderName = clientMessage.getSenderName();

        switch (messageType) {
            case ENTER:
                Channel enterChannel = channelService.findOneChannel(channelId);

                ServerMessage serverEnterMessage = new ServerMessage(RENEWAL, channelId, enterChannel.getUsers());
                redisPublisher.publish(channelRepository.getTopic(channelId), serverEnterMessage);

                GameInitMessage gameInitMessage = new GameInitMessage(INIT, channelId, enterChannel.getCompanyIds());
                redisPublisher.publish(channelRepository.getTopic(channelId), gameInitMessage);

                break;

            case EXIT:
                Channel exitChannel = channelRepository.findChannelById(channelId);
                ChannelState channelState = exitChannel.getChannelState();
                if(exitChannel.getHostId().equals(senderId) && channelState == WAITING) {
                    // Host가 퇴장하는데 대기방인 경우 -> 채널 삭제
                    System.out.println("exitChannel.getChannelName() = " + exitChannel.getChannelName());
                    ServerMessage serverCloseMessage = new ServerMessage(CLOSE, channelId, null);
                    redisPublisher.publish(channelRepository.getTopic(channelId), serverCloseMessage);
                    channelService.deleteChannel(exitChannel);
                }
                else {
                    if(channelState == STARTED && exitChannel.getUsers().isEmpty()) {
                        channelService.deleteChannel(exitChannel);
                    }
                    else {
                        // 채널 유지
                        Channel channel = channelService.exitChannel(channelId, senderId);
                        ServerMessage serverExitMessage = new ServerMessage(RENEWAL, channelId, channel.getUsers());
                        redisPublisher.publish(channelRepository.getTopic(channelId), serverExitMessage);
                    }
                }
                break;

            case READY:
                Channel readyChannel = channelService.setReady(channelId, senderId);
                if(readyChannel.getHostId().equals(senderId)) {
                    if(channelService.checkReadyState(channelId)) {
                        //모든인원 ready 상태 확인
                        ServerMessage serverStartTrueMessage = new ServerMessage(ServerMessageType.START, channelId, readyChannel.getUsers());
                        redisPublisher.publish(channelRepository.getTopic(channelId), serverStartTrueMessage);
                        gameStart(readyChannel);

                    }
                    else {
                        ServerMessage serverStartFalseMessage = new ServerMessage(NOTICE, channelId, readyChannel.getUsers());
                        redisPublisher.publish(channelRepository.getTopic(channelId), serverStartFalseMessage);
                    }
                }
                else {
                    ServerMessage serverReadyMessage = new ServerMessage(RENEWAL, channelId, readyChannel.getUsers());
                    redisPublisher.publish(channelRepository.getTopic(channelId), serverReadyMessage);
                }
                break;

            case CANCEL:
                Channel cancelChannel = channelService.cancelReady(channelId, senderId);
                ServerMessage serverCancelMessage = new ServerMessage(RENEWAL, channelId, cancelChannel.getUsers());
                redisPublisher.publish(channelRepository.getTopic(channelId), serverCancelMessage);
                break;
        }
    }

    //    @MessageMapping("/game/trade")
    public void gameStart(Channel channel) {
        String channelId = channel.getId();
        List<Long> allUsers = channel.getAllUsers();

        String profileServiceUrl = "http://profile-service:8080/api/v1/profile/point/bulk";
//
//        AllUserPointUpdate deduction = new AllUserPointUpdate(
//                allUsers,
//                channel.getEntryFee()
//        );
//        ResponseEntity<String> response = httpApiController.postRequest(profileServiceUrl, deduction);
//        if (!response.getStatusCode().equals(HttpStatus.OK)) {
//            ErrorMessage errorMessage = new ErrorMessage("");
//
//            return;
//        }

        // channel에 companyid들을 가지고 이쓴

        // 게임에서 사용할 기업을 랜덤으로 n개 가져온다.
//        HashSet<Long> companyIds = companyService.selectInGameCompany(2);

        // 게임에서 사용할 n개의 기업에 대한 주가 정보를 저장하는 배열
        List<List<StockInfo>> stockLists = new ArrayList<>();
        HashSet<Long> companyIds = channel.getCompanyIds();
        channel.setChannelState(STARTED);

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
                    int k = 0;
                    Iterator<Long> it = companyIds.iterator();
                    StockInfoMessages stockInfoMessages = new StockInfoMessages(STOCK, channelId);

                    while (it.hasNext()) {
                        Long next = it.next();
                        System.out.println("companyId = " + next);
                        StockInfo stockInfo = stockLists.get(k++).get(idx);
                        StockInfoMessage stockInfoMessage = new StockInfoMessage(
                                STOCK,
                                channelId,
                                stockInfo.getDate(),
                                stockInfo.getClose(),
                                stockInfo.getOpen(),
                                stockInfo.getHigh(),
                                stockInfo.getLow(),
                                stockInfo.getVolume(),
                                stockInfo.getCompany().getId()
                        );
                        System.out.println("stockInfo.getClose() = " + stockInfo.getClose());
                        Channel newChannel = channelService.findOneChannel(channelId);
                        newChannel.setCurrentPriceByCompany(next, stockInfo.getClose());
                        channelService.updateChannel(newChannel);

                        stockInfoMessages.addStockInfoMessage(stockInfoMessage);

                        closeValue.put(stockInfo.getCompany().getId(), stockInfo.getClose());
                    }

                    redisPublisher.publish(channelRepository.getTopic(channelId), stockInfoMessages);
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
        try {
            channelService.sellAllStock(closeValue, channel);
        } catch (Exception e) {
            System.out.println("====\nError in sellAllStock\n====");
        }


        // 게임 진행에 사용된 기업의 이름, 시작날짜, 종료 날짜를 반환한다.
        int k = 0;
        List<StockResult> stockResults = new ArrayList<>();
        for(Long cid: companyIds){
            Company company = companyService.findOneCompany(cid);
            StockResult stockResult = new StockResult(
                    cid,
                    company.getStock_name(),
                    stockLists.get(k).get(0).getDate(),
                    stockLists.get(k).get(59).getDate()
            );
            stockResults.add(stockResult);
            k++;
        }

        // 등수대로 포인트 차등 획득

        // 5명 참여, 입장료는 1인당 1,000 포인트
        // 5,000을 차등 부여
        // 1, 2, 3, 4, 5등
        // 1, 2, 3등만 부여한다고 가정?
        // 1등 2500, 2등 1500, 3등 1000(본전)
        // 나누는 기준을 정해야 함

//        AllUserPointUpdate updatePointByUser = new AllUserPointUpdate(
////                allUsers,
//                
//        );
//        ResponseEntity<String> stringResponseEntity = httpApiController.postRequest(profileServiceUrl, updatePointByUser);
//        Object obj = CustomJsonMapper.jsonParse(stringResponseEntity.getBody(), ResponseEntity.class);
//        ResponseEntity response = ResponseEntity.class.cast(obj);
//        if (!response.getStatusCode().equals(HttpStatus.OK) ) {
//            System.out.println("포인트 획득과정에서 문제가 발생했습니다.");
//        }

        Channel oneChannel = channelService.findOneChannel(channelId);
        StockGameEndMessage stockGameEndMessage = new StockGameEndMessage(
                CLOSE,
                channelId,
                stockResults,
                oneChannel.gameResult()
        );

        redisPublisher.publish(
                channelRepository.getTopic(channelId),
                stockGameEndMessage
        );

        channelService.deleteChannel(channel);
        System.out.println("Game End");
    }
}
