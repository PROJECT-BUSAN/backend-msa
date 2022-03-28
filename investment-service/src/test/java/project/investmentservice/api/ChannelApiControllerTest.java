package project.investmentservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import project.investmentservice.domain.Channel;
import project.investmentservice.dto.ChannelDto;
import project.investmentservice.dto.ChannelDto.CreateChannelRequest;
import project.investmentservice.dto.ChannelDto.EnterChannelRequest;
import project.investmentservice.service.ChannelService;

import java.util.HashSet;


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
        CreateChannelRequest createChannelRequest = createChannel();

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/investment/channel"));

        //then
        resultActions.andExpect(status().isOk());
        String channels = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("channels = " + channels);
    }

    @Test
    public void 채널생성API() throws Exception {
        //given
        CreateChannelRequest createChannelRequest = createChannel();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/channel")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createChannelRequest)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("channelName").value("newRoom"));
    }


    @Test
    public void 채널입장API() throws Exception {
        //given
        Channel testChannel1 = createChannelByService("TestChannel", 1L);
        Channel testChannel2 = createChannelByService("TestChannel", 2L);
        Channel testChannel3 = createChannelByService("TestChannel", 3L);

        EnterChannelRequest enterChannelRequest1 = new EnterChannelRequest();
        enterChannelRequest1.setUserId(10L);
        enterChannelRequest1.setUsername("admin");
        EnterChannelRequest enterChannelRequest2 = new EnterChannelRequest();
        enterChannelRequest2.setUserId(11L);
        enterChannelRequest2.setUsername("admin");
        EnterChannelRequest enterChannelRequest3 = new EnterChannelRequest();
        enterChannelRequest3.setUserId(12L);
        enterChannelRequest3.setUsername("admin");


        //when
        ResultActions resultActions1 = mockMvc.perform(post("/api/v1/investment/channel/" + testChannel1.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest1))
                .contentType(MediaType.APPLICATION_JSON));

        ResultActions resultActions2 = mockMvc.perform(post("/api/v1/investment/channel/" + testChannel2.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest2))
                .contentType(APPLICATION_JSON));

        ResultActions resultActions3 = mockMvc.perform(post("/api/v1/investment/channel/" + testChannel3.getId())
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


    private Channel createChannelByService(String name, Long userId){
        HashSet<Long> companyIds = new HashSet<>();
        companyIds.add(1L);
        companyIds.add(2L);

        return channelService.createChannel(name, 10, 100, userId, "admin", companyIds);
    }

    private CreateChannelRequest createChannel(){
        CreateChannelRequest createChannelRequest = new CreateChannelRequest();
        createChannelRequest.setName("newRoom");
        createChannelRequest.setEntryFee(100L);
        createChannelRequest.setUsername("admin");
        createChannelRequest.setUserId(1L);
        createChannelRequest.setLimitOfParticipants(10);
        return createChannelRequest;
    }

}

