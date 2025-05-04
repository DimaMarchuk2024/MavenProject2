package com.dima.integration.mapper;

import com.dima.enumPack.Role;
import com.dima.dto.UserReadDto;
import com.dima.entity.User;
import com.dima.mapper.UserReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserReadMapperTest {

    @Mock
    private UserReadMapper userReadMapper;

    @Test
    void mapSuccess() {
        User user = User.builder()
                .id(1L)
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .birthDate(LocalDate.of(2000, 4, 2))
                .build();
        UserReadDto expectedResult = UserReadDto.builder()
                .id(1L)
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .birthDate(LocalDate.of(2000, 4, 2))
                .build();
        doReturn(expectedResult).when(userReadMapper).map(user);

        UserReadDto actualResult = userReadMapper.map(user);

        verify(userReadMapper).map(user);
        assertThat(actualResult.getId()).isEqualTo(user.getId());
        assertThat(actualResult.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(actualResult.getLastname()).isEqualTo(user.getLastname());
        assertThat(actualResult.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(actualResult.getEmail()).isEqualTo(user.getEmail());
        assertThat(actualResult.getRole().name()).isEqualTo(user.getRole().name());
        assertThat(actualResult.getBirthDate()).isEqualTo(user.getBirthDate());
    }

    @Test
    void mapFail() {
        User user = User.builder()
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .build();
        UserReadDto expectedResult = UserReadDto.builder()
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("dummy")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .build();
        doReturn(expectedResult).when(userReadMapper).map(user);

        UserReadDto actualResult = userReadMapper.map(user);

        verify(userReadMapper).map(user);
        assertNotEquals(user.getPhoneNumber(), actualResult.getPhoneNumber());
    }
}
