package com.user.mapper;

import com.user.dto.UserDto;
import com.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by jiajia on 2017/8/4
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map(User user);

    User map(UserDto userDTO);

    List<UserDto> map(Iterable<User> users);

}
