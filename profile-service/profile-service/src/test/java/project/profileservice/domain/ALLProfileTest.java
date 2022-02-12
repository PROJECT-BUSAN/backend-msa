package project.profileservice.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ALLProfileTest {

    @Test
    public void profileALL_생성() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("investment");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ProfileBadge profileBadge1 = new ProfileBadge();
            ProfileBadge profileBadge2 = new ProfileBadge();
            ProfileInfo profileInfo = new ProfileInfo();
            Badge badge1 = new Badge();
            Badge badge2 = new Badge();

            badge1.getProfileBadges().add(profileBadge1);
            badge2.getProfileBadges().add(profileBadge2);

            profileInfo.getProfileBadges().add(profileBadge1);
            profileInfo.getProfileBadges().add(profileBadge2);


            em.persist(badge1);
            em.persist(badge2);
            em.persist(profileBadge1);
            em.persist(profileBadge2);
            em.persist(profileInfo);

            tx.commit();

        } catch (Exception e) {
            System.out.println("error = " + e);
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
