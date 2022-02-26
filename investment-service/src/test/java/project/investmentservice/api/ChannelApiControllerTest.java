package project.investmentservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static project.investmentservice.api.ChannelApiController.EnterChannelResponse.returnType.FAIL;
import static project.investmentservice.api.ChannelApiController.EnterChannelResponse.returnType.SUCCESS;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import project.investmentservice.api.ChannelApiController.CreateChannelRequest;
import project.investmentservice.api.ChannelApiController.EnterChannelRequest;
import project.investmentservice.domain.Channel;
import project.investmentservice.service.ChannelService;



@Disabled
@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ChannelApiControllerTest {

    @Autowired 
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ChannelService channelService;

    @Autowired
    private WebApplicationContext ctx;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();
    }

    @Test
    public void 모든채널반환API() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/game/channels")
                .contentType(APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
        String channels = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("channels = " + channels);
    }

    @Test
    public void 채널생성API() throws Exception {
        //given
        CreateChannelRequest createChannelRequest = new CreateChannelRequest("test채널", 10, 1000L, 31L);

        //when
        ResultActions resultActions = mockMvc.perform(post("/game/channel")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createChannelRequest)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("channelName").value("test채널"));
    }
    

    @Test
    public void 채널입장API() throws Exception {
        //given
        Channel channel1 = channelService.createChannel("testChannel32", 100, 20L, 51L);
        Channel channel2 = channelService.createChannel("testChannel32", 100, 100000L, 51L);
        Channel channel3 = channelService.createChannel("testChannel32", 1, 100000L, 51L);
        System.out.println("channel1.getId() : " + channel1.getId());
        EnterChannelRequest enterChannelRequest1 = new EnterChannelRequest();
        enterChannelRequest1.setUser_id(31L);
        EnterChannelRequest enterChannelRequest2 = new EnterChannelRequest();
        enterChannelRequest2.setUser_id(31L);
        EnterChannelRequest enterChannelRequest3 = new EnterChannelRequest();
        enterChannelRequest3.setUser_id(31L);

        //when
        ResultActions resultActions1 = mockMvc.perform(post("/game/channel/enter/" + channel1.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest1))
                .contentType(MediaType.APPLICATION_JSON));

        ResultActions resultActions2 = mockMvc.perform(post("/game/channel/enter/" + channel2.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest2))
                .contentType(APPLICATION_JSON));

        ResultActions resultActions3 = mockMvc.perform(post("/game/channel/enter/" + channel3.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest3))
                .contentType(APPLICATION_JSON));


        //then
        resultActions1.andExpect(status().isOk());
        resultActions2.andExpect(status().isOk());
        resultActions3.andExpect(status().isOk());

        String result1 = resultActions1.andReturn().getResponse().getContentAsString();
        System.out.println("result1 = " + result1);
        String result2 = resultActions2.andReturn().getResponse().getContentAsString();
        System.out.println("result2 = " + result2);
        String result3 = resultActions3.andReturn().getResponse().getContentAsString();
        System.out.println("result3 = " + result3);
    }
}

