package project.investmentservice.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.investmentservice.domain.Company;
import project.investmentservice.domain.StockInfo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class StockInfoRepository {
    @PersistenceContext
    private EntityManager em;

    /**
     * 테스트를 위한 save 메소드
     * 실제로는 django에서 cwaling을 할 때 값이 다 저장된다.
     */
    @Transactional
    public void save(StockInfo stockInfo) {
        em.persist(stockInfo);
    }
    
    public List<StockInfo> findStock(Long company_id) {
        return em.createQuery(
                "select s from StockInfo s " +
                        "where s.company.id =: company_id " +
                        "order by s.date"
                , StockInfo.class)
                .setParameter("company_id", company_id)
                .getResultList();
    }
}
