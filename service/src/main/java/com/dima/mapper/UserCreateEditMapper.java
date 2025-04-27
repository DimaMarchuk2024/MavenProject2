package com.dima.mapper;

import com.dima.dto.UserCreateEditDto;
import com.dima.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User>{

    private final PasswordEncoder passwordEncoder;

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

    private void copy(UserCreateEditDto userCreateEditDto, User user) {
        user.setFirstname(userCreateEditDto.getFirstname());
        user.setLastname(userCreateEditDto.getLastname());
        user.setPhoneNumber(userCreateEditDto.getPhoneNumber());
        user.setEmail(userCreateEditDto.getEmail());
        user.setRole(userCreateEditDto.getRole());
        user.setBirthDate(userCreateEditDto.getBirthDate());
        Optional.ofNullable(userCreateEditDto.getPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
    }
}
