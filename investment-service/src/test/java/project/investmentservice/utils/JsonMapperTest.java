package project.investmentservice.utils;

import org.junit.Test;
import project.investmentservice.api.ChannelApiController.EnterChannelRequest;

public class JsonMapperTest {

    @Test
    public void jsonMapperTest() {
        //given
        CustomJsonMapper customJsonMapper = new CustomJsonMapper();
        String strJson = "{\"userId\":\"123\", "
                + "\"username\":\"text\""
                + "}";

        //when
        EnterChannelRequest json = (EnterChannelRequest) customJsonMapper.jsonParse(strJson);

//        //then
        System.out.println("json = " + json);
    }
}
