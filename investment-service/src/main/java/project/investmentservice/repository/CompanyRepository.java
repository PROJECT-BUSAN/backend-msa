package project.investmentservice.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.investmentservice.domain.Company;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CompanyRepository {
    @PersistenceContext
    private EntityManager em;

    /**
     * 테스트를 위한 save 메소드
     * 실제로는 django에서 cwaling을 할 때 값이 다 저장된다.
     */
    @Transactional
    public void save(Company company) {

        em.persist(company);
    }

    public List<Company> findAll() {
        return em.createQuery("select c from Company c", Company.class).getResultList();
    }

    public Company findOne(Long company_id) {
        return em.createQuery("select c from Company c where c.id =: company_id"
                , Company.class)
                .setParameter("company_id", company_id)
                .getSingleResult();
    }
}
