package project.profileservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.profileservice.domain.ProfileBadge;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileBadgeRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(ProfileBadge profileBadge) {
        em.persist(profileBadge);
    }
    
    public List<ProfileBadge> findAllByUserId(Long id) {
        return em.createQuery("select p from ProfileBadge p where p.id =: profile_id", ProfileBadge.class)
                .setParameter("profile_id", id)
                .getResultList();
    }
}
