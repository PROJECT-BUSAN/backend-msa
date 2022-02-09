package project.profileservice.domain.profileinfo;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.ProfileInfo;
import project.profileservice.repository.ProfileInfoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddProfileBadgeTest {

    @Autowired
    ProfileInfoRepository profileInfoRepository;

    @Test
    public void addProfileBadge_Test() {
        //given
        ProfileInfo profileInfo = new ProfileInfo();
        ProfileBadge profileBadge = new ProfileBadge();

        //when
        profileInfo.addProfileBadge(profileBadge);
        profileInfoRepository.save(profileInfo);
        ProfileInfo findProfileInfo = profileInfoRepository.findOne(profileInfo.getId());

        //then
        Assertions.assertEquals(findProfileInfo.getId(), profileInfo.getId());
    }
}
