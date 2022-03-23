package project.investmentservice.utils;

import org.junit.Assert;
import org.junit.Test;
import project.investmentservice.dto.channel.EnterChannelRequest;

public class JsonMapperTest {

    @Test
    public void jsonMapperTest() {
        //given
        CustomJsonMapper customJsonMapper = new CustomJsonMapper();
        String strJson = "{" +
                "\"userId\":\"123\"," +
                "\"username\":\"null\"" +
                "}";

        //when
        Object obj = customJsonMapper.jsonParse(strJson, EnterChannelRequest.class);
        EnterChannelRequest enterChannelRequest = EnterChannelRequest.class
                .cast(obj);

        //then
        System.out.println("json = " + enterChannelRequest.getUsername());
        Assert.assertTrue(enterChannelRequest instanceof EnterChannelRequest);
        System.out.println("enterChannelRequest = " + enterChannelRequest);

    }
}
