package project.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.ProfileInfo;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.repository.ProfileInfoRepository;

@Service
@RequiredArgsConstructor
public class ProfileInfoService {

    private final ProfileInfoRepository profileInfoRepository;
    private final BadgeRepository badgeRepository;

    /**
     * profileInfo 추가
     */
    @Transactional
    public Long addProfileInfo(Long profileInfo_id, Long badge_id) {
        //엔티티 조회
        ProfileInfo profileInfo = profileInfoRepository.findOne(profileInfo_id);
        Badge badge = badgeRepository.findOne(badge_id);
        
        //ProfileBadge 생성
        ProfileBadge profileBadge = ProfileBadge.createProfileBadge(badge);

        //profileInfo 생성
        profileInfo.createProfileInfo(profileBadge);
        profileInfoRepository.save(profileInfo);
        return profileInfo.getId();
    }
}
