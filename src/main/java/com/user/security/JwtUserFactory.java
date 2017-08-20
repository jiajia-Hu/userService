package com.user.security;


import com.user.entity.User;

/**
 * Created by jiajia on 2017/8/4
 */

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getGroup()
        );
    }
}

