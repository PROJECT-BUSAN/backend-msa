package project.profileservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.ProfileInfo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileInfoRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void save(ProfileInfo profileInfo) {
        em.persist(profileInfo);
    }

    public ProfileInfo findOne(Long id) {
        return em.find(ProfileInfo.class, id);
    }

    public List<ProfileInfo> findAll() {
        return em.createQuery("select p from profile_info m", ProfileInfo.class).getResultList();
    }
}
