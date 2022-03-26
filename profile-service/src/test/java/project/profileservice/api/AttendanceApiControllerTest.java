package project.profileservice.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Profile;
import project.profileservice.repository.AttendanceRepository;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.repository.ProfileRepository;
import project.profileservice.service.AttendanceService;
import project.profileservice.service.ProfileService;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class AttendanceApiControllerTest extends BaseControllerTest {
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AttendanceService attendanceService;

    @Test
    public void 출석체크API() throws Exception {
        // given
        Long user_id = profileService.create(1L);
        Profile profile = attendanceService.findOneAttendanceAll(user_id);
        Attendance attendance = profile.getAttendances().get(0);
        attendance.setCreateAt(LocalDateTime.now().minusDays(1));
        
        // when
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/profile/1/attend")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }
}
