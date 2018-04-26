package cn.yejy.controller;

import cn.yejy.data.ResponseData;
import cn.yejy.entity.User;
import cn.yejy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/user/{user_id}")
    public ResponseEntity findOne(@PathVariable("user_id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseData.error(300,"empty");
        }
        return ResponseData.ok("success", user);
    }

    @GetMapping(value = "/user/all")
    public ResponseEntity findAll() {
        List<User> data = userService.find();
        return ResponseData.ok("ok", data);
    }
    @GetMapping(value = "/user/update/{user_id}")
    public ResponseEntity update(@PathVariable("user_id") Long id) {
        int rows = userService.update(id);
        if (rows == 1) {
            return ResponseData.ok("修改成功");
        } else {
            return ResponseData.error(400, "修改失败");
        }
    }
    @GetMapping(value = "/user/delete/{user_id}")
    public ResponseEntity delete(@PathVariable("user_id") Long id) {
        boolean result = userService.delete(id) == 1;
        if (result) {
            return ResponseData.ok("删除成功");
        } else {
            return ResponseData.error(400, "删除失败");
        }
    }

    @PostMapping(value = "/user/save")
    public ResponseEntity save(@RequestBody User user) {
        ResponseEntity result;
        if (user == null) {
            result = ResponseData.error(401, "缺少必要参数！");
        } else {
            User newUser = userService.save(user);
            if (newUser == null) {
                result = ResponseData.error(400, "保存失败");
            } else {
                result = ResponseData.ok("ok", user);
            }
        }
        return result;
    }

}
