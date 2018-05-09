package cn.yejy.controller;

import cn.yejy.data.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping(value = "/test/aa/bb")
    public ResponseEntity hello(@RequestParam("appid") String appid, @RequestParam("secret") String secret) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("appid", appid);
        map.put("secret", secret);
        return ResponseData.ok("success", map);
    }
}
