package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.investmentservice.api.InvestmentApiController.StockRequest;
import project.investmentservice.domain.Channel;
import project.investmentservice.domain.User;
import project.investmentservice.domain.UsersStock;
import project.investmentservice.repository.ChannelRepository;

@RequiredArgsConstructor
@Service
public class InvestmentService {

    @Autowired
    private final ChannelRepository channelRepository;

    /**
     * 비즈니스 로직
     * 주식 구매
     */
    public boolean purchaseStock(String channelId, StockRequest request) {
        Channel findChannel = channelRepository.findChannelById(channelId);

        Long requestCost = request.getCost();
        Long requestQuantity = request.getQuantity();
        User user = findChannel.getUsers().get(request.getUserId());
        Long userSeedMoney = user.getSeedMoney();
        UsersStock userCompanyStock = user.getCompanies().get(request.getCompanyId());

        if(userSeedMoney < requestCost * requestQuantity) {
            return false;
        }
        else {
            user.setSeedMoney(userSeedMoney - (requestCost * requestQuantity));
            if(user.getCompanies().containsKey(request.getCompanyId()) == false) {
                user.addCompany(request.getCompanyId());
            }
            Long newQuantity = userCompanyStock.getQuantity() + request.getQuantity();
            Long newTotalPrice = userCompanyStock.getTotalPrice() + (request.getCost() * request.getQuantity());

            userCompanyStock.renewalStock(((double)newTotalPrice / (double) newQuantity), newQuantity, newTotalPrice);
            channelRepository.updateChannel(findChannel);
            return true;
        }
    }

    /**
     * 비즈니스 로직
     * 주식 판매
     */
}
