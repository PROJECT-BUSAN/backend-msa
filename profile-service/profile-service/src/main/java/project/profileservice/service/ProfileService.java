package project.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.Profile;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.repository.ProfileBadgeRepository;
import project.profileservice.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final BadgeRepository badgeRepository;
    private final ProfileBadgeRepository profileBadgeRepository;

    // user_id를 통해 프로필 가져오기
    public Profile findOne(Long user_id) {
        return profileRepository.findOne(user_id);
    }

    // 모든 유저의 프로필 가져오기(관리자 페이지가 생긴다면 쓰일 듯하다)
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }
    
    // 프로필 생성(회원가입 시 함께 실행되어야 함)
    @Transactional
    public Long create(Long user_id) {
        Profile profile = new Profile();
        profile.setUser_id(user_id);
        profile.setStrick(1);
        profileRepository.save(profile);

        return profile.getUser_id();
    }

    // 프로필 삭제(회원 탈퇴 시 함께 실행되어야 함)
    @Transactional
    public void delete(Long user_id) {
        profileRepository.deleteById(user_id);
    }

    // 프로필 업데이트(현재 프로필에 수정할 만한 정보가 없어 구현 X)


    // 뱃지 획득
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
        profileBadgeRepository.save(profileBadge);
        
    }
    
}
