package project.profileservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.domain.Profile;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.service.ProfileService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class ProfileBadgeRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ProfileBadgeRepository profileBadgeRepository;

    @Autowired
    private ProfileService profileService;

    @Test
    public void findAllByUserId_Test() {
        //given
        Profile profile = createProfile();
        Badge badge1 = createBadge("새싹", "www.naver.com");
        Badge badge2 = createBadge("불", "www.google.com");
        
        //when
        List<ProfileBadge> findProfileBadges = profileBadgeRepository.findAllByUserId(profile.getId());

        //then
        for (ProfileBadge findProfileBadge : findProfileBadges) {
            System.out.println("findProfileBadge.getBadge() = " + findProfileBadge.getBadge().getName());
        }
    }

    @Test
    public void save_Test() {
        //given
        Profile profile = createProfile();
        Badge badge1 = createBadge("새싹", "www.naver.com");
        Badge badge2 = createBadge("불", "www.google.com");

        //when
        ProfileBadge profileBadge = new ProfileBadge();
        profileBadge.CreateProfileBadge(profile, badge1);
        profileBadge.CreateProfileBadge(profile, badge2);
        profileBadgeRepository.save(profileBadge);
        
        //then
        List<ProfileBadge> profileBadgeList = profileBadgeRepository.findAllByUserId(2L);
        Assertions.assertEquals(profileBadgeList.size(), 2);
    }

    private Profile createProfile() {
        Profile profile = new Profile();
        profile.setUser_id(2L);
        profile.setNowStrick(10);
        profile.setMaxStrick(15);
        
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
