package com.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.entity.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/**
 * Created by jiajia on 2017/8/4
 */
@Data
@EqualsAndHashCode
@ToString
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private Set<String> roles;
    private Group group;
}
