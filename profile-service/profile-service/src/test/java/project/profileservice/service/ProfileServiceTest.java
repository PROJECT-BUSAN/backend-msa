package project.profileservice.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Profile;
import project.profileservice.service.ProfileService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProfileServiceTest {
    
    @PersistenceContext
    EntityManager em;

    @Autowired
    private ProfileService profileService;
    
    @Test
    public void 프로필생성() throws Exception{
        // given
        Profile profile = new Profile();
        profile.setUser_id(1L);
        profile.setNowStrick(1);
        profile.setMaxStrick(1);
        
        //when
        Long user_id = profileService.create(1L);

        //then
        Assertions.assertEquals(user_id, 1L);
    }
    
}
