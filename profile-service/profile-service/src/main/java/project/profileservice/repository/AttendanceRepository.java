package project.profileservice.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AttendanceRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Attendance 객체를 DB에 저장한다.
     * @param attendance
     */
    @Transactional
    public void save(Attendance attendance) {
        em.persist(attendance);
    }
    
}
