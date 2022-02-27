package project.investmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.investmentservice.api.InvestmentApiController.StockRequest;
import project.investmentservice.domain.Channel;
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
        Long userSeedMoney = findChannel.getUsers().get(request.getUserId()).getSeedMoney();

        if(findChannel.getUsers().get(request.getUserId()).getSeedMoney() < requestCost * requestQuantity) {
            return false;
        }
        else {
            findChannel.getUsers().get(request.getUserId()).setSeedMoney(userSeedMoney - (requestCost * requestQuantity));
            if(findChannel.getUsers().get(request.getUserId()).getCompanies().containsKey(request.getCompanyId()) == false) {
                findChannel.getUsers().get(request.getUserId()).addCompany(request.getCompanyId());
            }
            Long newQuantity = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getQuantity() + request.getQuantity();
            Long newPrefixSum = findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).getPrefixSum() + (request.getCost() * request.getQuantity());

            findChannel.getUsers().get(request.getUserId()).getCompanies().get(request.getCompanyId()).renewalStock(((double)newPrefixSum / (double) newQuantity), newQuantity, newPrefixSum);
            channelRepository.updateChannel(findChannel);
            return true;
        }
    }

    /**
     * 비즈니스 로직
     * 주식 판매
     */
}
