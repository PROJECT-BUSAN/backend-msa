package project.investmentservice.repository;

import org.springframework.stereotype.Repository;
import project.investmentservice.domain.Company;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CompanyRepository {
    @PersistenceContext
    private EntityManager em;

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
