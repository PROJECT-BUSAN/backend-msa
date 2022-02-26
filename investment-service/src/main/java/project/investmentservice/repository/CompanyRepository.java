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
}
