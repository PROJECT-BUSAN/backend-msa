package project.profileservice.service.profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.domain.Profile;
import project.profileservice.service.ProfileService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class AddBadgeTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ProfileService profileService;

//    @Test
//    public void profile_생성() {
//        //given
//        Badge badge = createBadge("새싹", "www.naver.com");
//
//        //when
//        Long profile_id = profileService.addProfile(2L, badge.getId());
//        System.out.println("profile_id = " + profile_id);
//        //then
//
//    }

    @Test
    public void addBadgeTest() {
        //given
        Profile profile = createProfile();
        Badge badge = createBadge("새싹", "www.naver.com");

        //when
        Long profileInfoId = profileService.addBadge(profile.getId(), badge.getId());
        Profile getProfile = em.find(Profile.class, profileInfoId);

        //then
        for(ProfileBadge hasProfileBadge : getProfile.getProfileBadges()) {
            System.out.println("hasProfileBadge.getBadge().getName() = " + hasProfileBadge.getBadge().getName());
        }
    }

    private Profile createProfile() {
        Profile profile = new Profile();
        profile.setUser_id(2L);
        profile.setStrick(10);
        em.persist(profile);
        return profile;
    }


    private Badge createBadge(String name, String url) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setImage_url(url);
        em.persist(badge);
        return badge;
    }
}
