package project.pointservice.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointLogTest {

    @Test
    public void PrintLog_생성() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("investment");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            PointLog pointLog = new PointLog();
            em.persist(pointLog);

            Point point = new Point();
            point.getPointLogs().add(pointLog);
            em.persist(point);

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
