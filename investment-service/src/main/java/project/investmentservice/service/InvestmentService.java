package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.investmentservice.api.InvestmentApiController.StockRequest;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.domain.dto.ServerMessage;
import project.investmentservice.repository.ChannelRepository;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import static project.investmentservice.domain.dto.ServerMessage.MessageType.RENEWAL;

@RequiredArgsConstructor
@Service
public class InvestmentService {
    
    private final ChannelRepository channelRepository;

    /**
     * 비즈니스 로직
     * 종목 구매
     */
    public boolean purchaseStock(String channelId, StockRequest request) {
        Channel findChannel = channelRepository.findChannelById(channelId);
        User user = findChannel.getUsers().get(request.getUserId());
        
        // 구매를 원하는 종목의 가격, 수량
        double requestPrice = request.getPrice();
        Long requestQuantity = request.getQuantity();
        
        // 현재 유저의 시드머니
        double userSeedMoney = user.getSeedMoney();

        // 원하는 만큼의 주식을 살 수 없다면 false 리턴
        if(userSeedMoney < requestPrice * requestQuantity) {
            return false;
        }
        else {
            // 구매 가격만큼 유저의 시드머니를 감소시킴
            user.setSeedMoney(userSeedMoney - (requestPrice * requestQuantity));
            
            // 유저의 보유 종목을 추가함
            if(user.getCompanies().containsKey(request.getCompanyId()) == false) {
                user.addCompany(request.getCompanyId());
            }

            UsersStock usersStock = user.getCompanies().get(request.getCompanyId());
            
            // 유저가 보유한 종목의 보유 수량과 값을 업데이트
            Long newQuantity = usersStock.getQuantity() + request.getQuantity();
            double newTotalPrice = usersStock.getTotalPrice() + (request.getPrice() * request.getQuantity());

            usersStock.renewalStock((newTotalPrice / (double)newQuantity), newQuantity, newTotalPrice);
            channelRepository.updateChannel(findChannel);
            return true;
        }
    }

    /**
     * 비즈니스 로직
     * 종목 판매
     */
    public boolean sellStock(String channelId, StockRequest request) {
        Channel findChannel = channelRepository.findChannelById(channelId);
        User user = findChannel.getUsers().get(request.getUserId());

        // 매도 가격, 수량
        double requestPrice = request.getPrice();
        Long requestQuantity = request.getQuantity();
        
        // 현재 유저의 시드머니, 평균 매수 가격
        double userSeedMoney = user.getSeedMoney();
        double averagePrice = user.getCompanies().get(request.getCompanyId()).getAveragePrice();
        
        // 현재 유저가 매도하려는 종목
        UsersStock usersStock = user.getCompanies().get(request.getCompanyId());

        // 현재 유저가 매도하려는 종목의 보유 수량
        Long quantity = usersStock.getQuantity();
        // 현재 유저가 매도하려는 종목의 총 가격
        double totalPrice = usersStock.getTotalPrice();

        // 매도 수량이 보유 수량보다 많다면 false 리턴
        if(requestQuantity > quantity) {
            return false;
        }
        else {
            // (종목 가격 - 평균 구매 가격) * 매도 수량 = 시드머니 변동값
            user.setSeedMoney((userSeedMoney + (requestPrice - averagePrice) * requestQuantity));
            if(quantity == requestQuantity) {
                usersStock.renewalStock(0.0, 0L, 0.0);
            }
            else {
                usersStock.renewalStock(averagePrice, quantity - requestQuantity, totalPrice-(requestPrice * requestQuantity));
            }
            return true;
        }
    }
}
