package project.investmentservice.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CustomJsonMapper {

    public JsonElement jsonParse(String str) {
        System.out.println("str = " + str);
        JsonParser parser = new JsonParser();
        JsonElement obj = parser.parse(str);
        System.out.println("obj = " + obj);
        return obj;
    }
}
