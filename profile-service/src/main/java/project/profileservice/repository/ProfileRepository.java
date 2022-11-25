package project.profileservice.repository;

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

    /**
     * 새로운 프로필을 저장한다.
     * (회원가입시 딱 한번 수행될 것)
     * @param profile
     */
    @Transactional
    public void save(Profile profile) {
        em.persist(profile);
    }

    /**
     * user_id를 통해 프로필을 찾고, 삭제한다.
     * @param user_id
     */
    @Transactional
    public void deleteById(Long user_id) {
        Profile profile = findOne(user_id);
        em.remove(profile);
    }

    /**
     * 조회 시 사용하지 말 것
     * 사용처 : remove, Test
     */
    public Profile findOne(Long user_id) {
        Profile profile = em.createQuery(
                        "select p from Profile p " +
                                "where p.user_id = :user_id", Profile.class)
                .setParameter("user_id", user_id)
                .getSingleResult();

        return profile;
    }

    /**
     * 유저의 모든 정보(배지 현황, 출석 현황)를 join을 통해 한 번에 가져온다
     * @param user_id
     * @return Profile
     */
    public Profile findOneAllInfo(Long user_id) {
        Profile profile = em.createQuery(
                "select p from Profile p " +
                        "left join p.profileBadges " +
                        "left join p.attendances " +
                        "where p.user_id = :user_id", Profile.class)
                .setParameter("user_id", user_id)
                .getSingleResult();

        return profile;
    }

    /**
     * @param user_id
     * @return Profile
     */
    public Profile findOneAttendanceTop(Long user_id) {
        Profile profile = em.createQuery(
                        "select p from Profile p " +
                                "left join fetch p.attendances as attends " +
                                "where p.user_id = :user_id " +
                                "order by attends.createAt desc", Profile.class)
                .setParameter("user_id", user_id)
                .setFirstResult(0)
                .setMaxResults(1)
                .getSingleResult();

        return profile;
    }

    /**
     * 유저의 기본 정보와 출석 현황만 fetch join을 통해 한 번에 가져온다.
     * @param user_id
     * @return Profile
     */
    public Profile findOneAttendanceAll(Long user_id) {
        Profile profile = em.createQuery(
                        "select p from Profile p " +
                                "left join fetch p.attendances as attends " +
                                "where p.user_id = :user_id " +
                                "order by attends.createAt desc", Profile.class)
                .setParameter("user_id", user_id)
                .getSingleResult();

        return profile;
    }

    /**
     * 유저의 기본 정보와 배지 현황만 fetch join을 통해 한 번에 가져온다.
     * @param user_id
     * @return Profile
     */
    public Profile findOneBadgeAll(Long user_id) {
        Profile profile = em.createQuery(
                        "select p from Profile p " +
                                "left join p.profileBadges " +
                                "where p.user_id = :user_id " , Profile.class)
                .setParameter("user_id", user_id)
                .getSingleResult();

        return profile;
    }

    /**
     * 모든 유저의 프로필을 가져온다.
     * (쓸 일이 있을까? 모르겠다.)
     * @return List<Profile>
     */
    public List<Profile> findAll() {
        return em.createQuery("select p from Profile p", Profile.class).getResultList();
    }
}
