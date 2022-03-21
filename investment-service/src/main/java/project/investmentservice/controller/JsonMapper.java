package project.investmentservice.controller;

import com.google.gson.JsonParser;
import org.json.simple.JSONObject;

public class JsonMapper {

    public Object jsonParse(String str) {
        System.out.println("str = " + str);
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(str);
        System.out.println("obj = " + obj);
        return obj;
    }
}
