package project.investmentservice.repository;

import org.springframework.stereotype.Repository;
import project.investmentservice.domain.StockInfo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StockInfoRepository {
    @PersistenceContext
    private EntityManager em;

    public List<StockInfo> findSpecificStock(Long company_id) {
        return em.createQuery(
                "select s from StockInfo s " + 
                        "where s.company =: company_id", StockInfo.class)
                .setParameter("company_id", company_id)
                .getResultList();
    }
}
