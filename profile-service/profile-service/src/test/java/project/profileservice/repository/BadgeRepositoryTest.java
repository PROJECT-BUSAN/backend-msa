package project.profileservice.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BadgeRepositoryTest {

    @Autowired
    private BadgeRepository badgeRepository;

    @Test
    public void badge_저장() {
        //given
        ProfileBadge profileBadge = new ProfileBadge();
        Badge badge = new Badge();
        badge.setName("새싹");
        badge.setImage_url("www.naver.com");
        badge.getProfileBadges().add(profileBadge);

        //when
        badgeRepository.save(badge);

        //then

    }
}
