package project.profileservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.Profile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void profile_저장() {
        //given
        ProfileBadge profileBadge = new ProfileBadge();

        Profile profile = new Profile();
        profile.setUser_id(1L);
        profile.setStrick(10);
        profile.AddProfileBadge(profileBadge);

        //when
        profileRepository.save(profile);
        Profile findProfile = profileRepository.findOne_ByProfileId(1L);

        //then
        Assertions.assertEquals(profile.getStrick(), findProfile.getStrick());
    }
}
