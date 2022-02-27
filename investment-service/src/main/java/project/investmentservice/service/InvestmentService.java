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
     * 종목 구매
     */
    public boolean purchaseStock(String channelId, StockRequest request) {
        Channel findChannel = channelRepository.findChannelById(channelId);

        double requestPrice = request.getPrice();
        Long requestQuantity = request.getQuantity();
        double userSeedMoney = findChannel.getUsers().get(request.getUserId()).getSeedMoney();

        if(findChannel.getUsers().get(request.getUserId()).getSeedMoney() < requestPrice * requestQuantity) {
            return false;
        }
        else {
            findChannel.getUsers().get(request.getUserId()).setSeedMoney(userSeedMoney - (requestPrice * requestQuantity));
            if(findChannel.getUsers().get(request.getUserId()).getCompanies().containsKey(request.getCompanyId()) == false) {
                findChannel.getUsers().get(request.getUserId()).addCompany(request.getCompanyId());
            }
            Long newQuantity = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getQuantity() + request.getQuantity();
            double newTotalPrice = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getTotalPrice() + (request.getPrice() * request.getQuantity());

            findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).renewalStock((newTotalPrice / (double)newQuantity), newQuantity, newTotalPrice);
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

        double requestPrice = request.getPrice();
        Long requestQuantity = request.getQuantity();

        double userSeedMoney = findChannel.getUsers().get(request.getUserId()).getSeedMoney();
        double averagePrice = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getAveragePrice();
        Long quantity = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getQuantity();
        double totalPrice = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getTotalPrice();

        if(requestQuantity > quantity) {
            return false;
        }
        else {
            findChannel.getUsers().get(request.getUserId()).setSeedMoney((userSeedMoney + (requestPrice- averagePrice) * requestQuantity));
            if(quantity == requestQuantity) {
                findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).renewalStock(0.0, 0L, 0.0);
            }
            else {
                findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).renewalStock(averagePrice, quantity - requestQuantity, totalPrice-(requestPrice * requestQuantity));
            }
            return true;
        }
    }
}
