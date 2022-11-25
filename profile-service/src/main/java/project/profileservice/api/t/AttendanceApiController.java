package project.profileservice.api.t;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Profile;
import project.profileservice.service.AttendanceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class AttendanceApiController {
    
    private final AttendanceService attendanceService;

    @GetMapping("/{user_id}/attend")
    @ApiOperation(value = "현재 유저의 전체 출석현황 조회", notes = "현재 유저의 출석현황을 조회한다.")
    public List<AttendanceResult> AttendanceListV1(@PathVariable("user_id") Long user_id) {
        Profile profile = attendanceService.findOneAttendanceAll(user_id);
        
        List<Attendance> attendances = profile.getAttendances();
        List<AttendanceResult> result = attendances.stream()
                .map(a -> new AttendanceResult(a.getCreateAt()))
                .collect(Collectors.toList());

        return result;
    }

    @PostMapping("/{user_id}/attend")
    @ApiOperation(value = "출석 체크", notes = "user_id에 해당하는 유저가 출석체크한다.")
    public List<AttendanceResult> AttendV1(@PathVariable("user_id") Long user_id) {
        attendanceService.attend(user_id);
        
        Profile profile = attendanceService.findOneAttendanceAll(user_id);
        List<Attendance> attendances = profile.getAttendances();
        List<AttendanceResult> result = attendances.stream()
                .map(a -> new AttendanceResult(a.getCreateAt()))
                .collect(Collectors.toList());

        return result;
    }

    @Data
    @AllArgsConstructor
    static class AttendanceResult {
        private LocalDateTime createAt;
    }
}
