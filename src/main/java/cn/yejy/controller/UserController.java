package cn.yejy.controller;

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
            return ResponseEntity.ok("empty");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/user/all")
    public List<User> findAll() {
        return userService.find();
    }
    @GetMapping(value = "/user/update/{user_id}")
    public ResponseEntity update(@PathVariable("user_id") Long id) {
        int rows = userService.update(id);
        if (rows == 1) {
            return ResponseEntity.ok("修改成功");
        } else {
            return ResponseEntity.ok("修改失败");
        }
    }
    @GetMapping(value = "/user/delete/{user_id}")
    public String delete(@PathVariable("user_id") Long id) {
        return userService.delete(id) == 1 ? "失败成功" : "删除失败";
    }

    @GetMapping(value = "/user/save")
    public String save() {
        return  "新增ID为：" + userService.save();
    }



}
