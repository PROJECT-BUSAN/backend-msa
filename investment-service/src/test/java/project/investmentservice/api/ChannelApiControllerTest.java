package project.investmentservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.investmentservice.api.ChannelApiController.CreateChannelRequest;
import project.investmentservice.api.ChannelApiController.EnterChannelRequest;
import project.investmentservice.domain.Channel;
import project.investmentservice.service.ChannelService;


@RunWith(SpringRunner.class)
@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class ChannelApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChannelService channelService;

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

        EnterChannelRequest enterChannelRequest1 = new EnterChannelRequest(31L);
        EnterChannelRequest enterChannelRequest2 = new EnterChannelRequest(31L);
        EnterChannelRequest enterChannelRequest3 = new EnterChannelRequest(31L);

        //when
        ResultActions resultActions1 = mockMvc.perform(post("/game/channel/enter/" + channel1.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest1))
                .contentType(APPLICATION_JSON));

        ResultActions resultActions2 = mockMvc.perform(post("/game/channel/enter/" + channel2.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest2))
                .contentType(APPLICATION_JSON));

        ResultActions resultActions3 = mockMvc.perform(post("/game/channel/enter/" + channel3.getId())
                .content(new ObjectMapper().writeValueAsString(enterChannelRequest3))
                .contentType(APPLICATION_JSON));


        //then
        resultActions1.andExpect(status().isOk())
                .andExpect(jsonPath("type").value(FAIL))
                .andExpect(jsonPath("message").value("채널에 입장하기 위한 포인트가 부족합니다."));

        resultActions2.andExpect(status().isOk())
                .andExpect(jsonPath("type").value(SUCCESS))
                .andExpect(jsonPath("message").value("채널에 입장합니다."));

        resultActions3.andExpect(status().isOk())
                .andExpect(jsonPath("type").value(FAIL))
                .andExpect(jsonPath("message").value("채널에 인원이 가득찼습니다."));

        String result = resultActions1.andReturn().getResponse().getContentAsString();
        System.out.println("result = " + result);
    }
}

