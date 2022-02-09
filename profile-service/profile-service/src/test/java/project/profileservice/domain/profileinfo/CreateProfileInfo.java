package project.profileservice.domain.profileinfo;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.ProfileInfo;
import project.profileservice.repository.ProfileInfoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateProfileInfo {

    @Autowired
    ProfileInfoRepository profileInfoRepository;

    @Test
    public void createProfileInfo_Test() {
        //given
        ProfileInfo profileInfo = new ProfileInfo();
        ProfileBadge profileBadge1 = new ProfileBadge();
        ProfileBadge profileBadge2 = new ProfileBadge();

        //when
        profileInfo.createProfileInfo(profileBadge1, profileBadge2);

        //then
        for(ProfileBadge profileBadge : profileInfo.getProfileBadges()) {
            Assertions.assertEquals(profileBadge.getProfileInfo().getId(), profileInfo.getId());
        }
    }
}
