package project.profileservice.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.profileservice.domain.Badge;
import project.profileservice.domain.ProfileBadge;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BadgeRepositoryTest {

    @Autowired
    private BadgeRepository badgeRepository;

    @Test
    public void save_Test() {
        //given
        Badge badge = new Badge();
        badge.setName("새싹");
        badge.setImage_url("www.naver.com");

        //when
        badgeRepository.save(badge);
        Badge findBadge = badgeRepository.findOne(badge.getId());

        //then
        Assertions.assertEquals(findBadge.getName(), badge.getName());
        Assertions.assertEquals(findBadge.getImage_url(), badge.getImage_url());
    }

    @Test
    public void findOne_Test() {
        //given
        Badge badge = new Badge();
        badge.setName("새싹");
        badge.setImage_url("www.naver.com");
        badgeRepository.save(badge);

        //when
        Badge findBadge = badgeRepository.findOne(badge.getId());

        //then
        Assertions.assertEquals(findBadge.getName(), badge.getName());
        Assertions.assertEquals(findBadge.getImage_url(), badge.getImage_url());
    }

    @Test
    public void findAll() {
        //given
        Badge badge1 = new Badge();
        badge1.setName("새싹");
        Badge badge2 = new Badge();
        badge2.setName("불");
        badgeRepository.save(badge1);
        badgeRepository.save(badge2);

        //when
        List<Badge> badges = badgeRepository.findAll();

        //then
        Assertions.assertEquals(badges.size(), 2);
    }


}
