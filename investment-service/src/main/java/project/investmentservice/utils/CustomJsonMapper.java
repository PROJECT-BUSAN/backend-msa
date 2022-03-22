package project.investmentservice.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import project.investmentservice.api.ChannelApiController;
import project.investmentservice.api.ChannelApiController.EnterChannelRequest;

@Component
public class CustomJsonMapper {

    public Object jsonParse(String jsonStr, Class className) {
        Gson gson = new Gson();
        Object obj = new Object();
        try {
            obj = gson.fromJson(jsonStr, className);
        } catch (Exception e) {
            throw new RuntimeException("JSON 형식이 잘못 되었습니다. 필드명이 없거나 올바르지 않습니다.");
        }
        return obj;
    }
}
