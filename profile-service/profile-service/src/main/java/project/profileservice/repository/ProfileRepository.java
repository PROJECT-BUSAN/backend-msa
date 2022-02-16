package project.profileservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProfileRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Profile profile) {
        em.persist(profile);
    }

    @Transactional
    public void deleteById(Long user_id) {
        Profile profile = findOne(user_id);
        em.remove(profile);
    }

    public Profile findOne(Long user_id) {
        Profile profile = em.createQuery(
                "select p from Profile p" +
                        " left join fetch p.profileBadges" +
                        " where p.user_id = :user_id", Profile.class)
                .setParameter("user_id", user_id)
                .getSingleResult();

        return profile;
    }

    public List<Profile> findAll() {
        return em.createQuery("select p from Profile p", Profile.class).getResultList();
    }
}
