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

    /**
     * Profile과 Badge의 다대다 연결을 위한 중계 테이블인 ProfileBadge를 생성한다.
     * @param profileBadge
     */
    public void save(ProfileBadge profileBadge) {
        em.persist(profileBadge);
    }


    /**
     * ProfileBadge 테이블만 가져오는 경우는 없을 것 같다.
     * 만약 Profile을 조회하면서 Profile과 연결된 Badge도 함께 가져오고 싶다면,
     * 이 메서드를 사용하지 말것.
     * 대신 ProfileRepository.findOneBadgeAll 를 사용하자.
     * @param id
     * @return
     */
    public List<ProfileBadge> findAllByUserId(Long id) {
        return em.createQuery("select p from ProfileBadge p where p.id =: profile_id", ProfileBadge.class)
                .setParameter("profile_id", id)
                .getResultList();
    }
}
