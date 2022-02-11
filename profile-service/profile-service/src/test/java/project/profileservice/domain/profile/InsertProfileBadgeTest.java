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
public class InsertProfileBadgeTest {

    @Autowired
    ProfileRepository profileRepository;

    @Test
    public void InsertProfileBadge_Test() {
        //given
        Profile profile = new Profile();
        ProfileBadge profileBadge = new ProfileBadge();

        //when
        profile.InsertProfileBadge(profileBadge);
        profileRepository.save(profile);
        Profile findProfile = profileRepository.findOne_ByProfileId(profile.getId());

        //then
        Assertions.assertEquals(findProfile.getId(), profile.getId());
    }
}
