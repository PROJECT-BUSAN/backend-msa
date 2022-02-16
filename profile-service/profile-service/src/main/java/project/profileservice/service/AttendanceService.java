package project.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Profile;
import project.profileservice.repository.AttendanceRepository;
import project.profileservice.repository.ProfileRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ProfileRepository profileRepository;

    /**
     * user_id에 해당하는 유저의 기본 정보와 전체 출석 현황을 가져온다.
     * @param user_id
     * @return Profile
     */
    public Profile findOneAttendanceAll(Long user_id) {
        return profileRepository.findOneAttendanceAll(user_id);
    }

    /**
     * user_id에 해당하는 유저의 출석체크를 수행한다.
     * 출석을 했다면 
     * @param user_id
     * @return
     */
    @Transactional
    public void attend(Long user_id) {
        // 엔티티 조회
        Profile profile = profileRepository.findOneAttendanceAll(user_id);
        
        // Attend 가능한지 확인(이미 출석한 경우 출석 X, 오류취급은 하지 않음)
        if (!validAttend(profile)) {
            return;
        }
        // 출석이 가능하다면 strick 업데이트 수행
        strickUpdate(profile);
        
        // Attendance 생성
        Attendance attendance = new Attendance();
        attendance.setCreateAt(LocalDateTime.now());
        attendance.setProfile(profile);
        
        // Attendance 저장
        profile.addAttendance(attendance);
        attendanceRepository.save(attendance);
    }

    /**
     * 유저의 연속 출석 일수를 업데이트 한다.
     * @param profile
     */
    public void strickUpdate(Profile profile) {
        // nowStrick은 연속으로 출석한 경우에만 올라간다.
        // 연속 출석이 깨지면 nowStrick은 다시 1부터 시작한다.
        // maxStrick은 nowStrick의 최대치를 저장해둔다.
        
        Attendance attendance = profile.getAttendances().get(0);
        Period period = Period.between(attendance.getCreateAt().toLocalDate(), LocalDate.now());
        
        if (period.getDays() == 1) {
            profile.nowStrickUp();
            profile.maxStrickUpdate();
        } else if(period.getDays() >= 2) {
            profile.nowStrickReset();
        }
    }

    /**
     * 유저가 출석이 가능한지 여부를 확인한다.
     * @param profile
     * @return
     */
    public boolean validAttend(Profile profile) {
        // 오늘 출석 했는지 확인.
        // 이미 출석을 했다면 false return
        // 아니라면 true return
        Attendance attendance = profile.getAttendances().get(0);
        if (attendance.getCreateAt().toLocalDate() == LocalDate.now()) {
            return false;
        } else {
            return true;
        }
    }
    
    
    
}
