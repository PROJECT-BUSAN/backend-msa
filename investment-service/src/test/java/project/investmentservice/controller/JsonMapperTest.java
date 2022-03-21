package project.investmentservice.controller;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.investmentservice.api.ChannelApiController;
import project.investmentservice.api.ChannelApiController.EnterChannelRequest;
import project.investmentservice.domain.Channel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonMapperTest {

    @Test
    public void jsonMapperTest() {
        //given
        JsonMapper jsonMapper = new JsonMapper();
        String strJson = "{\"userId\":\"123\", "
                + "\"username\":\"text\""
                + "}"
                + "}";

        //when
        EnterChannelRequest json = (EnterChannelRequest) jsonMapper.jsonParse(strJson);

        //then
        System.out.println("json = " + json);
    }
}
