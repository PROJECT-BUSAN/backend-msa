package project.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.Profile;
import project.profileservice.repository.AttendanceRepository;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.repository.ProfileBadgeRepository;
import project.profileservice.repository.ProfileRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final BadgeRepository badgeRepository;
    private final ProfileBadgeRepository profileBadgeRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * user_id를 통해 프로필의 '모든' 정보를 가져온다.
     * @param user_id
     * @return Profile
     */
    public Profile findOneAllInfo(Long user_id) {
        return profileRepository.findOneAllInfo(user_id);
    }

    /**
     * 모든 유저의 프로필을 가져온다.(관리자 페이지가 생긴다면 쓰일 듯하다)
     * @return
     */
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    /**
     * 초기 프로필 생성(회원가입 시 반드시 함께 실행되어야 함)
     * @param user_id
     * @return Long
     */
    @Transactional
    public Long create(Long user_id) {
        // profile 생성
        Profile profile = new Profile();
        profile.setUser_id(user_id);
        profile.setMaxStrick(1);
        profile.setNowStrick(1);
        profile.setPoint(1000000L);
        
        // Attendance 생성
        Attendance attendance = new Attendance();
        attendance.setCreateAt(LocalDateTime.now());
        attendance.setProfile(profile);
        
        // profile에 attendance 추가
        profile.addAttendance(attendance);
        
        // Attendance 저장
        attendanceRepository.save(attendance);
        // Profile 저장
        profileRepository.save(profile);

        return profile.getUser_id();
    }

    /**
     * 프로필 삭제
     * (회원 탈퇴 시 함께 실행되어야 함)
     * @param user_id
     */
    @Transactional
    public void delete(Long user_id) {
        profileRepository.deleteById(user_id);
    }

    // 프로필 업데이트(현재 프로필에 수정할 만한 정보가 없어 구현 X)


    /**
     * 뱃지 획득
     * (뱃지 획득 루트는 이후 논의 필요)
     * @param user_id
     * @param badge_id
     */
    @Transactional
    public void obtainBadge(Long user_id, Long badge_id) {
        // user_ud에 해당하는 profile이 badge_id에 해당하는 badge를 획득한다.
        
        // 엔티티 조회
        Profile profile = profileRepository.findOne(user_id);
        Badge badge = badgeRepository.findOne(badge_id);
        
        // ProfileBadge 생성
        ProfileBadge profileBadge = new ProfileBadge();
        profileBadge.CreateProfileBadge(profile, badge);
        
        // ProfileBadge 저장
        profile.addProfileBadge(profileBadge);
        badge.addProfileBadge(profileBadge);
        profileBadgeRepository.save(profileBadge);
    }

    /**
     * Point 업데이트
     * @param user_id
     * @param point
     * @return
     */
    @Transactional
    public Long updatePoint(Long user_id, Long point) {
        Profile profile = profileRepository.findOne(user_id);
        profile.updatePoint(point);
        return profile.getPoint();
    }
}
