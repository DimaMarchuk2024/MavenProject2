package com.dima.mapper;

import com.dima.dto.UserCreateEditDto;
import com.dima.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User>{

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);

        return toObject;
    }

    @Override
    public User map(UserCreateEditDto userCreateEditDto) {
        User user = new User();
        copy(userCreateEditDto, user);

        return user;
    }

    private static void copy(UserCreateEditDto userCreateEditDto, User user) {
        user.setFirstname(userCreateEditDto.getFirstname());
        user.setLastname(userCreateEditDto.getLastname());
        user.setPhoneNumber(userCreateEditDto.getPhoneNumber());
        user.setEmail(userCreateEditDto.getEmail());
        user.setRole(userCreateEditDto.getRole());
        user.setBirthDate(userCreateEditDto.getBirthDate());
        user.setPassword(userCreateEditDto.getPassword());
    }
}
