package project.profileservice.service.profileinfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.ProfileInfo;
import project.profileservice.repository.ProfileInfoRepository;
import project.profileservice.service.ProfileInfoService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class AddProfileInfoTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ProfileInfoService profileInfoService;

    @Test
    public void profileInfo_생성() {
        //given
        Badge badge = createBadge("새싹", "www.naver.com");

        //when
        Long profile_id = profileInfoService.addProfileInfo(2L, badge.getId());
        System.out.println("profile_id = " + profile_id);
        //then

    }

    @Test
    public void addProfileInfoTest() {
        //given
        ProfileInfo profileInfo = createProfileInfo();
        Badge badge = createBadge("새싹", "www.naver.com");

        //when
        Long profileInfoId = profileInfoService.addProfileInfo(profileInfo.getId(), badge.getId());
        ProfileInfo getProfileInfo = em.find(ProfileInfo.class, profileInfoId);

        //then
        for(ProfileBadge hasProfileBadge : getProfileInfo.getProfileBadges()) {
            System.out.println("hasProfileBadge.getBadge().getName() = " + hasProfileBadge.getBadge().getName());
        }
    }

    private ProfileInfo createProfileInfo() {
        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setUser_id(2L);
        profileInfo.setStrick(10);
        em.persist(profileInfo);
        return profileInfo;
    }


    private Badge createBadge(String name, String url) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setImage_url(url);
        em.persist(badge);
        return badge;
    }
}
