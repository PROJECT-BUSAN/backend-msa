package project.profileservice.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Profile;
import project.profileservice.repository.ProfileRepository;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
@Rollback(value = false)
public class AttendanceServiceTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileService profileService;

    @Test
    @Transactional
    public void attend_Test() {
        //given
        Long user_id = profileService.create(2L);
        Profile profile = attendanceService.findOneAttendanceAll(user_id);
        Attendance attendance = profile.getAttendances().get(0);
        attendance.setCreateAt(LocalDateTime.of(2022, 2, 10, 10, 10, 10));

        //when
        attendanceService.attend(user_id);
        
        //then
        // profile 영속성 유지
        Assertions.assertEquals(profile.getAttendances().size(), 2);
    }
    
    @Test
    @Transactional
    public void NoAttend_이미출석한경우_Test() {
        //given
        Long user_id = profileService.create(2L);
        
        //when
        attendanceService.attend(user_id);
        Profile profile = attendanceService.findOneAttendanceAll(user_id);

        //then
        // 이미 출석했다면 출석이 증가되면 안된다.
        Assertions.assertEquals(profile.getAttendances().size(), 1);
    }

    @Test
    @Transactional
    public void 연속출석Test() {
        //given
        Long user_id = profileService.create(2L);
        Profile profile = attendanceService.findOneAttendanceAll(user_id);
        Attendance attendance = profile.getAttendances().get(0);
        attendance.setCreateAt(LocalDateTime.now().minusDays(1));

        //when
        attendanceService.attend(user_id);
        
        //then
        Assertions.assertEquals(profile.getMaxStrick(), 2);
        Assertions.assertEquals(profile.getNowStrick(), 2);

    }
    
}
