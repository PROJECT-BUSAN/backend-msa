package project.profileservice.domain.profile;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.Profile;
import project.profileservice.repository.ProfileRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddProfileBadgeTest {

    @Autowired
    ProfileRepository profileRepository;

    @Test
    public void AddProfileBadge_Test() {
        //given
        Profile profile = new Profile();
        ProfileBadge profileBadge1 = new ProfileBadge();
        ProfileBadge profileBadge2 = new ProfileBadge();

        //when
        profile.AddProfileBadge(profileBadge1, profileBadge2);

        //then
        for(ProfileBadge profileBadge : profile.getProfileBadges()) {
            Assertions.assertEquals(profileBadge.getProfile().getId(), profile.getId());
        }
    }
}
