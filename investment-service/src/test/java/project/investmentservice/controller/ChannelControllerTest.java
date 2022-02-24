package project.investmentservice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ChannelController.class)
public class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void channels() throws Exception{
        //given
        String hello = "hello";
        mockMvc.perform(get("/game/hello"))	// (5)
                .andExpect(status().isOk())	// (6)(7)
                .andExpect(content().string(hello));	// (8)

        //when

        //then
    }
}
