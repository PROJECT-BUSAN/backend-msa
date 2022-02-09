package project.profileservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Profile;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {

    private final EntityManager em;

    @Transactional
    public void save(Profile profile) {
        em.persist(profile);
    }

    public Profile findOne_ByProfileId(Long id) {
        return em.find(Profile.class, id);
    }

    public Profile findOne_ByUserId(Long id) {
        return em.find(Profile.class, 1L);
    }

    public List<Profile> findAll() {
        return em.createQuery("select p from profile m", Profile.class).getResultList();
    }
}
