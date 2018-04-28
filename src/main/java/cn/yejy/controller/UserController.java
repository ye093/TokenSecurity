package cn.yejy.controller;

import cn.yejy.data.ResponseData;
import cn.yejy.service.UserService;
import cn.yejy.util.UserHolderUtil;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/user")
    public ResponseEntity findOne(HttpServletRequest request) {
        Integer id = UserHolderUtil.getUserId(request);
        Record user = userService.findById(id);
        if (user == null) {
            return ResponseData.error(300,"empty");
        }
        return ResponseData.ok("success", user.intoMap());
    }

    @GetMapping(value = "/user/all")
    public ResponseEntity findAll() {
        List<Map<String, Object>> data = userService.find();
        return ResponseData.ok("ok", data);
    }
    @PostMapping(value = "/user/update/{user_id}")
    public ResponseEntity update(@PathVariable("user_id") Integer id, @RequestParam("username") String username,
                                 @RequestParam("password") String password) {
        int rows = userService.update(id, username, password);
        if (rows == 1) {
            return ResponseData.ok("修改成功");
        } else {
            return ResponseData.error(400, "修改失败");
        }
    }
    @PostMapping(value = "/user/delete/{user_id}")
    public ResponseEntity delete(@PathVariable("user_id") Integer id) {
        boolean result = userService.delete(id) == 1;
        if (result) {
            return ResponseData.ok("删除成功");
        } else {
            return ResponseData.error(400, "删除失败");
        }
    }

    @PostMapping(value = "/user/save")
    public ResponseEntity save(@RequestParam("username") String username, @RequestParam("mobile") String mobile,
                               @RequestParam("password") String password) {
        ResponseEntity result;
        Record userRecord = userService.save(username, mobile, password, true, false, false);
        if (userRecord == null) {
            result = ResponseData.error(400, "保存失败");
        } else {
            result = ResponseData.ok("ok", userRecord.intoMap());
        }
        return result;
    }

}
