package project.profileservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.Profile;

import java.util.List;

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

    @Test
    public void findOne_ByProfileId_Test() {

        //given
        Profile profile = new Profile();

        //when
        profileRepository.save(profile);
        Profile findProfile = profileRepository.findOne_ByProfileId(profile.getId());

        //then
        Assertions.assertEquals(findProfile.getId(), profile.getId());
    }

    @Test
    public void findOne_ByUserId_Test() {
        //given
        Profile profile = new Profile();
        profile.setUser_id(2L);
        profileRepository.save(profile);

        //when
        Profile findProfile = profileRepository.findOne_ByUserId(profile.getUser_id()).get(0);

        //then
        Assertions.assertEquals(findProfile.getId(), profile.getId());

    }

    @Test
    public void findAll_Test() {

        //given
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        profileRepository.save(profile1);
        profileRepository.save(profile2);

        //when
        List<Profile> findProfiles = profileRepository.findAll();

        //then
        Assertions.assertEquals(findProfiles.size(), 4);
    }
}
