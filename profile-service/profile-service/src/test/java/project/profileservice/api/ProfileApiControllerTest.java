package project.profileservice.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import project.profileservice.domain.Badge;
import project.profileservice.domain.Profile;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.repository.ProfileRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.profileservice.api.ProfileApiController.*;

@RunWith(SpringRunner.class)
public class ProfileApiControllerTest extends BaseControllerTest {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BadgeRepository badgeRepository;

    @Test
    public void 프로필생성API() throws Exception {
        // given
        CreateProfileRequest createProfileRequest = new CreateProfileRequest();
        createProfileRequest.setUser_id(1L);
        
        // when
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(createProfileRequest)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("user_id").value(1L));
    }

    @Test
    public void 프로필조회API() throws Exception {
        // given
        createProfile(1L);
        
        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/v1/profile/1")
                .contentType(MediaType.APPLICATION_JSON));
        
        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void 뱃지획득API() throws Exception{
        // given
        createProfile(1L);
        createBadge("BADGE");
        
        // when
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/profile/1/badge/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("획득 성공"));
    }

    private void createProfile(Long user_id) {
        Profile profile = new Profile();
        profile.setUser_id(user_id);
        profile.setStrick(1);
        profileRepository.save(profile);
    }

    private void createBadge(String name) {
        Badge badge = new Badge();
        badge.setImage_url("https://...");
        badge.setName(name);
        badgeRepository.save(badge);
    }
}
