package project.profileservice.repository;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Profile;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@Transactional(readOnly = true)
@SpringBootTest
public class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @Transactional
    public void 출석객체저장() {
        //given
        Profile profile = new Profile();
        profile.setMaxStrick(1);
        profile.setUser_id(1L);
        profile.setNowStrick(1);
        
        Attendance attendance = new Attendance();
        attendance.setProfile(profile);
        attendance.setCreateAt(LocalDateTime.now());

        profile.addAttendance(attendance);
        
        //when
        profileRepository.save(profile);
        attendanceRepository.save(attendance);
        Profile findProfile = profileRepository.findOneAttendanceAll(1L);
        System.out.println("findProfile.getId() = " + findProfile.getId());
        System.out.println("findProfile.getAttendances().size() = " + findProfile.getAttendances().size());
        
        //then
        Assertions.assertEquals(findProfile.getAttendances().get(0).getId(), attendance.getId());
    }
    
}
