package project.profileservice.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.profileservice.domain.Badge;
import project.profileservice.domain.Profile;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.repository.ProfileRepository;
import project.profileservice.service.ProfileService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileApiController {

    @Autowired
    private final ProfileService profileService;
    @Autowired
    private final ProfileRepository profileRepository;
    @Autowired
    private final BadgeRepository badgeRepository;

    @GetMapping("/api/v1/profile/{id}")
    public TestProfile profileV1(@PathVariable("id") Long user_id) {

        Profile profile = new Profile();
        profile.setUser_id(user_id); // user_id에 해당하는 Profile 가져온 상황을 가정
        profile.setStrick(10);

        profileRepository.save(profile);

        Badge badge1 = new Badge();
        badge1.setName("새싹");
        Badge badge2 = new Badge();
        badge2.setName("불");
        // Badge가 2개 DB에 존재한다고 가정
        badgeRepository.save(badge1);
        badgeRepository.save(badge2);

        profileService.addBadge(profile.getId(), badge1.getId());
        profileService.addBadge(profile.getId(), badge2.getId());

        TestProfile testProfile = new TestProfile();
        testProfile.CreateTestProfile(profile);
        return testProfile;
    }

    @Data
    static class TestProfile {
        private Long profile_id;
        private Long user_id;
        private int strick;
        private List<Long> badges_id = new ArrayList<>();

        private void CreateTestProfile(Profile profile) {
            this.profile_id = profile.getId();
            this.user_id = profile.getUser_id();
            this.strick = profile.getStrick();
            for(ProfileBadge profileBadge : profile.getProfileBadges()) {
                badges_id.add(profileBadge.getBadge().getId());
            }
        }
    }

}
