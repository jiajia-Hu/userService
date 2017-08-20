package com.user.controller;

import com.google.common.base.Preconditions;
import com.user.dto.UserDto;
import com.user.entity.User;
import com.user.param.UserParam;
import com.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiajia on 2017/8/4
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册")
    @POST
    @Path("/registered")
    public UserDto registeredUser(@ApiParam UserParam userParam) {
        String email = userParam.getEmail();
        String password = userParam.getPassword();
        boolean checkEmail = userService.checkEmail(email);
        Preconditions.checkArgument(checkEmail, "邮箱格式不对");
        User user = userService.getUserFromEmail(email);
        Preconditions.checkArgument(user == null, "该用户帐号已经注册，请直接登录");
        UserDto userDto = userService.insert(email, password);
        userService.sendEmail(email);
        return userDto;
    }

    @ApiOperation(value = "发送激活邮件", notes = "这个接口返回当前用户的token")
    @GET
    @RequestMapping(value = "/sendEmail")
    public String sendEmail(@QueryParam("email") String email) {
        boolean checkEmail = userService.checkEmail(email);
        Preconditions.checkArgument(checkEmail, "邮箱格式不对");
        User user = userService.getUserFromEmail(email);
        Preconditions.checkArgument(user != null, "帐号有误，找不到该用户");
        return userService.sendEmail(email);
    }

    @ApiOperation(value = "验证激活帐号", notes = "传入的参数当前用户的一个token")
    @GET
    @Path("/active")
    public void activeUser(@QueryParam("authorize") String authorize) {
        Boolean internal = userService.tokenInUse(authorize);
        Preconditions.checkArgument(!internal, "token 过期，请重新激活");
        User user = userService.getUserFromToken(authorize);
        boolean active = userService.checkActive(user);
        Preconditions.checkArgument(!active, "该用户帐号已经激活");
        userService.active(user);
    }

    @ApiOperation(value = "登录", notes = "返回一个token")
    @POST
    @Path("/login")
    public Map<String, String> login(@ApiParam UserParam userParam) {
        String email = userParam.getEmail();
        String password = userParam.getPassword();
        boolean checkEmail = userService.checkEmail(email);
        Preconditions.checkArgument(checkEmail, "邮箱格式不对");
        User user = userService.getUserFromEmail(email);
        Preconditions.checkArgument(user != null, "该用户还未注册，请先注册");
        boolean pass = userService.checkPass(user, password);
        Preconditions.checkArgument(pass, "密码错误");
        boolean active = userService.checkActive(user);
        Preconditions.checkArgument(active, "该用户帐号未激活");
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", userService.getToken(user.getEmail()));
        map.put("username", user.getUsername());
        map.put("userGroup", user.getGroup().getGroupRole());
        return map;
    }


    @ApiOperation(value = "修改密码")
    @POST
    @Path("/changePass")
    public UserDto changePass(@ApiParam UserParam userParam) {
        String email = userParam.getEmail();
        String password = userParam.getPassword();
        boolean checkEmail = userService.checkEmail(email);
        Preconditions.checkArgument(checkEmail, "邮箱格式不对");
        User user = userService.getUserFromEmail(email);
        return userService.changePass(password, user);
    }


    @ApiOperation(value = "刷新token")
    @GET
    @Path("/refreshToken")
    public Map<String, String> refresh() {
        String token = userService.refresh();
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return map;
    }
}
