package com.dima.integration.mapper;

import com.dima.Enum.Role;
import com.dima.dto.UserReadDto;
import com.dima.entity.User;
import com.dima.integration.annotation.IT;
import com.dima.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@IT
@RequiredArgsConstructor
public class UserReadMapperTest {
    
    private final UserReadMapper userReadMapper;

    @Test
    void mapSuccess() {
        User user = User.builder()
                .id(1L)
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .build();
        UserReadDto expectedResult = UserReadDto.builder()
                .id(1L)
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .build();

        UserReadDto actualResult = userReadMapper.map(user);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void mapFail() {
        User user = User.builder()
                .id(1L)
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .build();
        UserReadDto expectedResult = UserReadDto.builder()
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .build();

        UserReadDto actualResult = userReadMapper.map(user);

        assertNotEquals(actualResult, expectedResult);
    }
}
