package com.user.service;


import com.user.dto.UserDto;
import com.user.entity.Group;
import com.user.entity.User;
import com.user.mapper.UserMapper;
import com.user.repository.GroupRepository;
import com.user.repository.UserRepository;
import com.user.util.JwtTokenUtil;
import com.user.util.PasswordUtil;
import com.user.util.SendMail;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by jiajia on 2017/8/4
 */


@Log4j2
@Service
public class UserServiceImpl implements UserService {


    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SendMail sendMail;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailServiceImp userDetailService;


    @Override
    public Group getEBaoRole(String roleName) {
        return groupRepository.findByGroupRole(roleName);
    }

    //注册 邮箱注册
    @Override
    public UserDto insert(String number, String password) {
        User user = new User();
        String salt = PasswordUtil.getSalt();
        String hashPassword = PasswordUtil.getHash(password, salt);//加密
        user.setSalt(salt);
        user.setPassword(hashPassword);
        user.setEmail(number);
        user.setUsername(number);
        Group group = groupRepository.findByGroupRole("ROLE_USER");
        user.setGroup(group);
        user.setActive(false);
        userRepository.save(user);
        return mapper.map(user);
    }

    @Override
    public boolean checkEmail(String email) {
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //发送激活邮件
    @Override
    public String sendEmail(String email) {
        String tokenString = getToken(email);
        try {
            sendMail.sendTemplateMail(email, tokenString);//发送激活邮件
        } catch (Exception e) {
           // log.warn("邮件发送失败", e);
        }
        return tokenString;
    }

    //激活帐号
    @Override
    public void active(User user) {
        user.setActive(true);//激活
        userRepository.save(user);
    }

    //token 过期
    @Override
    public boolean tokenInUse(String token) {
        return jwtTokenUtil.isTokenExpired(token);
    }

    @Override
    public User getUserFromToken(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserFromEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //帐号是否激活
    @Override
    public boolean checkActive(User user) {
        return user.isActive();
    }

    //帐号登录密码是否正确
    @Override
    public boolean checkPass(User user, String password) {
        String localPass = user.getPassword();//数据库里面的密码
        String salt = user.getSalt();
        return PasswordUtil.verify(localPass, password, salt);//验证
    }

    //修改密码
    @Override
    public UserDto changePass(String password,User user) {
        String salt = PasswordUtil.getSalt();
        String hashPassword = PasswordUtil.getHash(password, salt);//加密
        user.setSalt(salt);
        user.setPassword(hashPassword);
        userRepository.save(user);
        return mapper.map(user);
    }


    @Override
    public String refresh() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public String getToken(String email) {

        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return jwtTokenUtil.generateToken(userDetails);
    }


}
