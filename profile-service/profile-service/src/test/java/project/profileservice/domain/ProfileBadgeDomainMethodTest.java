package project.profileservice.domain;

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
public class ProfileBadgeDomainMethodTest {

    @Autowired
    ProfileRepository profileRepository;
    

    @Test
    public void AddProfileBadge_Test() {
        //given
        Profile profile = new Profile();
        ProfileBadge profileBadge = new ProfileBadge();

        //when
        profile.addProfileBadge(profileBadge);
        profileRepository.save(profile);
        Profile findProfile = profileRepository.findOne(profile.getId());

        //then
        Assertions.assertEquals(findProfile.getId(), profile.getId());
    }

    @Test
    public void CreateProfileBadge_Test() {
        //given
        Badge badge = new Badge();
        badge.setName("새싹");
        Profile profile = new Profile();
        profile.setUser_id(3L);
        
        //when
        ProfileBadge profileBadge = new ProfileBadge();
        profileBadge.CreateProfileBadge(profile, badge);
        badge.addProfileBadge(profileBadge);
        profile.addProfileBadge(profileBadge);
        
        //then
        Assertions.assertEquals(profileBadge.getBadge().getName(), badge.getName());
        Assertions.assertEquals(badge.getProfileBadges().get(0).getId(), profileBadge.getId());
    }
}
