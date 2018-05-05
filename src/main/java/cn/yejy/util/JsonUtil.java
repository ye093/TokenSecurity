package cn.yejy.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {


    public static Map<String, Object> parse(String text) {
        Map<String, Object> data = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            data = mapper.readValue(text, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
