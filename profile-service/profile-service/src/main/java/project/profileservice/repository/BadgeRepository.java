package project.profileservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BadgeRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Badge badge) {
        em.persist(badge);
    }

    @Transactional
    public void deleteById(Long badge_id) {
        Badge badge = findOne(badge_id);
        em.remove(badge);
    }

    public Badge findOne(Long id) {
        return em.find(Badge.class, id);
    }

    public List<Badge> findAll() {
        return em.createQuery("select b from Badge b", Badge.class).getResultList();
    }
}
