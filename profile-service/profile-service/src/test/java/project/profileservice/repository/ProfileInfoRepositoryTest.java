package project.profileservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.ProfileInfo;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfileInfoRepositoryTest {

    @Autowired
    private ProfileInfoRepository profileInfoRepository;

    @Test
    public void profileinfo_저장() {
        //given
        ProfileBadge profileBadge = new ProfileBadge();

        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setUser_id(1L);
        profileInfo.setStrick(10);
        profileInfo.getProfileBadges().add(profileBadge);

        //when
        profileInfoRepository.save(profileInfo);
        ProfileInfo findProfileInfo = profileInfoRepository.findOne(1L);

        //then
        Assertions.assertEquals(profileInfo.getStrick(), findProfileInfo.getStrick());
    }



}
