package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.dto.InvestmentDto;
import project.investmentservice.dto.InvestmentDto.StockRequest;
import project.investmentservice.dto.InvestmentDto.TradeRequest;
import project.investmentservice.dto.SocketDto;
import project.investmentservice.exceptions.InsufficientPointException;
import project.investmentservice.exceptions.SellOverHoldStockException;
import project.investmentservice.repository.ChannelRepository;


@RequiredArgsConstructor
@Service
public class InvestmentService {
    
    private final ChannelRepository channelRepository;
    /**
     * 비즈니스 로직
     * 종목 구매
     */
    public void purchaseStock(String channelId, TradeRequest request) {
        // Service에서 사용할 값들 get
        Long companyId = request.getCompanyId();
        Long userId = request.getUserId();
        double quantity = request.getQuantity();

        Channel findChannel = channelRepository.findChannelById(channelId);
        User user = findChannel.getUsers().get(userId);
        double currentPrice = findChannel.getCurrentPriceByCompany(companyId);

        // 현재 유저의 시드머니
        double userSeedMoney = user.getSeedMoney();

        // 원하는 만큼의 주식을 살 수 없다면 false 리턴
        if(userSeedMoney < currentPrice * quantity) {
            throw new InsufficientPointException("보유 금액보다 주문량이 많습니다.");
        }
        // 구매 가격만큼 유저의 시드머니를 감소시킴
        user.setSeedMoney(userSeedMoney - (currentPrice * quantity));
        
        // 첫 구매라면 유저의 보유 종목에 추가함
        if(!user.getCompanies().containsKey(companyId)) {
            user.addCompany(companyId);
        }

        UsersStock usersStock = user.getCompanies().get(companyId);
        
        // 유저가 보유한 종목의 보유 수량과 값을 업데이트
        double newQuantity = usersStock.getQuantity() + quantity;
        double newTotalPrice = usersStock.getTotalPrice() + (currentPrice * quantity);

        usersStock.renewalStock((newTotalPrice / newQuantity), newQuantity, newTotalPrice);
        channelRepository.updateChannel(findChannel);
    }

    /**
     * 비즈니스 로직
     * 종목 판매
     */
    public void sellStock(String channelId, TradeRequest request) {
        Long userId = request.getUserId();
        Long companyId = request.getCompanyId();
        double requestQuantity = request.getQuantity();

        Channel findChannel = channelRepository.findChannelById(channelId);
        User user = findChannel.getUsers().get(userId);

        // 매도 가격, 수량
        double currentPrice = findChannel.getCurrentPriceByCompany(companyId);

        // 현재 유저의 시드머니, 평균 매수 가격
        double userSeedMoney = user.getSeedMoney();
        double averagePrice = user.getCompanies().get(companyId).getAveragePrice();
        
        // 현재 유저가 매도하려는 종목
        UsersStock usersStock = user.getCompanies().get(companyId);

        // 현재 유저가 매도하려는 종목의 보유 수량
        double quantity = usersStock.getQuantity();
        // 현재 유저가 매도하려는 종목의 총 가격
        double totalPrice = usersStock.getTotalPrice();

        // 매도 수량이 보유 수량보다 많다면 false 리턴
        if(requestQuantity > quantity) {
            throw new SellOverHoldStockException("매도 수량이 보유 수량보다 많습니다");
        }
        
        // (종목 가격 - 평균 구매 가격) * 매도 수량 = 시드머니 변동값
        user.setSeedMoney(userSeedMoney + (currentPrice * requestQuantity));
        if(quantity == (requestQuantity)) {
            usersStock.renewalStock(0.0, 0.0, 0.0);
        }
        else {
            usersStock.renewalStock(averagePrice, quantity - requestQuantity, totalPrice-(averagePrice * requestQuantity));
        }
        channelRepository.updateChannel(findChannel);
    }



}
