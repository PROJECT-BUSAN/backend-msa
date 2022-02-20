package project.profileservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.profileservice.service.BadgeService;
import project.profileservice.service.ProfileService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.profileservice.api.ProfileApiController.*;

@RunWith(SpringRunner.class)
public class ProfileApiControllerTest extends BaseControllerTest {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private BadgeService badgeService;

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
        Long profileId = createProfile(1L);
        Long badgeId = createBadge("BADGE");

        // when
        ResultActions resultActions = this.mockMvc.perform(
                post("/api/v1/profile/" + profileId + "/badge/" + badgeId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("획득 성공"));
    }

    @Test
    public void 포인트업데이트API() throws Exception{
        // given
        Long profileId = createProfile(1L);
        UpdatePointRequest updatePointRequest = new UpdatePointRequest();
        updatePointRequest.setPoint(-500000L);
        // when
        ResultActions resultActions = this.mockMvc.perform(
                post("/api/v1/profile/" + profileId + "/point")
                        .content(new ObjectMapper().writeValueAsString(updatePointRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("point").value("500000"));
    }
    
    

    private Long createProfile(Long user_id) {
        return profileService.create(user_id);
    }

    private Long createBadge(String name) {
        return badgeService.create(name, "https://...");
    }
}
