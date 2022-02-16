package project.profileservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import project.profileservice.domain.Badge;
import project.profileservice.repository.BadgeRepository;
import project.profileservice.service.BadgeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(SpringRunner.class)
public class BadgeApiControllerTest extends BaseControllerTest{

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private BadgeService badgeService;
    
    @Test
    public void 모든Badge가져오기() throws Exception {
        // given
        createBadge("A");
        createBadge("B");
        createBadge("C");
        
        //when
        ResultActions resultActions = this.mockMvc.perform(get("/api/v1/profile/badge")
                .contentType(MediaType.APPLICATION_JSON));
        
        //then
        resultActions
                .andDo(print());
    }
    
    private void createBadge(String name) {
        Badge badge = new Badge();
        badge.setImage_url("https://...");
        badge.setName(name);
        badgeRepository.save(badge);
    }
}
