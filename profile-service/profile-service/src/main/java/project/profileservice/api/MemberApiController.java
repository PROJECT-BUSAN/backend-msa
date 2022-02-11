package project.profileservice.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.profileservice.domain.Badge;
import project.profileservice.domain.Profile;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.repository.ProfileRepository;
import project.profileservice.service.ProfileService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    @Autowired
    private final ProfileService profileService;
    @Autowired
    private final ProfileRepository profileRepository;

    @PersistenceContext
    EntityManager em;

    @GetMapping("/api/v1/profile/{id}")
    public TestProfile profileV1(@PathVariable("id") Long user_id) {
        TestProfile testProfile = new TestProfile();

        testProfile.setId(1L);
        testProfile.setStrick(10);

        return testProfile;
    }

    @Data
    static class TestProfile {
        private Long id;
        private int strick;
        private List<ProfileBadge> profileBadges;
    }

}
