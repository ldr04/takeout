package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.myvamp.vamp.common.CustomException;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.entity.User;
import com.myvamp.vamp.service.UserService;
import com.myvamp.vamp.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public R<String> senMsg(@RequestBody User user, HttpSession httpSession){

        String phone = user.getPhone();
        log.info("phone:{}",phone);
        if(StringUtils.checkValNotNull(phone)){
            Integer code = ValidateCodeUtils.generateValidateCode(4);

            log.info("code:{}",code);
            httpSession.setAttribute("code",code);
            return R.success("发送成功");
        }

        return R.error("发送失败");

    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpServletRequest request){

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        System.out.println(phone);
        System.out.println(code);

        LambdaQueryWrapper<User> userlqw = new LambdaQueryWrapper<>();
        userlqw.eq(User::getPhone,phone);

        User user = userService.getOne(userlqw);
        if(user==null){
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }
        userlqw.eq(User::getStatus,1);
        user = userService.getOne(userlqw);
        if(user==null){
            throw new CustomException("账号被封禁");
        }
        request.getSession().setAttribute("user",user.getId());
        return R.success(user);

    }





}
