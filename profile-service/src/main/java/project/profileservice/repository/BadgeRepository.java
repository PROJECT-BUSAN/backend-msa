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

    /**
     * Badge 객체를 DB에 저장한다
     * (관리자가 Badge를 생성하는 경우 수행된다.)
     * @param badge
     */
    @Transactional
    public void save(Badge badge) {
        em.persist(badge);
    }

    /**
     * baded_id를 통해 Badge를 찾고, 삭제한다.
     * @param badge_id
     */
    @Transactional
    public void deleteById(Long badge_id) {
        Badge badge = findOne(badge_id);
        em.remove(badge);
    }

    /**
     * id에 맞는 badge를 찾는다
     * @param id
     * @return Badge
     */
    public Badge findOne(Long id) {
        return em.find(Badge.class, id);
    }

    /**
     * 현재 DB에 저장된 모든 Badge 종류를 조회한다. 
     * @return List<badge> 
     */
    public List<Badge> findAll() {
        return em.createQuery("select b from Badge b", Badge.class).getResultList();
    }
}
