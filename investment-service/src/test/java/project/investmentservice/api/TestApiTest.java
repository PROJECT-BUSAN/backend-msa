package project.investmentservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import project.investmentservice.api.TestApiController.TestLoginRequired;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();
    }
    
    @Test
    public void LoginRequiredOnTest() throws Exception {
        // given
        TestLoginRequired loginRequest = new TestLoginRequired(1L, "admin");
        
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/test/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        System.out.println("resultActions = " + resultActions);
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(1L))
                .andExpect(jsonPath("username").value("admin"));
    }


    @Test
    public void LoginRequiredOFFTest() throws Exception {
        // given
        TestLoginRequired loginRequest = new TestLoginRequired(1L, "admin");

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/investment/test/login")
                .contentType(APPLICATION_JSON));

        //then
        System.out.println("resultActions = " + resultActions.andReturn());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 로그인필요but_No_userId() throws Exception {
        // given
        TestLoginRequired loginRequest = new TestLoginRequired(null, "");

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/test/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        resultActions.andExpect(status().isForbidden());
    }
}
