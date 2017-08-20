package com.user.service;


import com.user.dto.UserDto;
import com.user.entity.Group;
import com.user.entity.User;

/**
 * Created by jiajia on 2017/8/4
 */
public interface UserService {

    //注册邮箱
    UserDto insert(String number, String password);

    boolean checkEmail(String email);


    //发送激活邮件
    String sendEmail(String email);


    //激活邮箱帐号
    void active(User user);
    boolean tokenInUse(String token);
    User getUserFromToken(String token);

    //登录
    User getUserFromEmail(String email);
    boolean checkActive(User user);
    boolean checkPass(User user, String password);

    //修改密码
    UserDto changePass(String password, User user);

    //刷新token
    String refresh();

    String getToken(String email);
    Group getEBaoRole(String roleName);

}
