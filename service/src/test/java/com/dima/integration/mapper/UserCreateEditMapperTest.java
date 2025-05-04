package com.dima.integration.mapper;

import com.dima.enumPack.Role;
import com.dima.dto.UserCreateEditDto;
import com.dima.entity.User;
import com.dima.mapper.UserCreateEditMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserCreateEditMapperTest {

    @Mock
    private UserCreateEditMapper userCreateEditMapper;

    @Test
    void mapSuccess() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder()
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .password("555")
                .build();
        User expectedResult = User.builder()
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .password("555")
                .build();
        doReturn(expectedResult).when(userCreateEditMapper).map(userCreateEditDto);

        User actualResult = userCreateEditMapper.map(userCreateEditDto);

        verify(userCreateEditMapper).map(userCreateEditDto);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void mapFail() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder()
                .firstname("Vova")
                .lastname("Vovan")
                .phoneNumber("12-34-567")
                .email("vova@gmail.com")
                .role(Role.ADMIN)
                .password("555")
                .build();
        User expectedResult = User.builder()
                .firstname("Vova1")
                .lastname("Vovan1")
                .phoneNumber("76-54-321")
                .email("vova1@gmail.com")
                .role(Role.USER)
                .password("777")
                .build();
        doReturn(expectedResult).when(userCreateEditMapper).map(userCreateEditDto);

        User actualResult = userCreateEditMapper.map(userCreateEditDto);

        verify(userCreateEditMapper).map(userCreateEditDto);
        assertNotEquals(userCreateEditDto.getFirstname(), actualResult.getFirstname());
        assertNotEquals(userCreateEditDto.getLastname(), actualResult.getLastname());
        assertNotEquals(userCreateEditDto.getPhoneNumber(), actualResult.getPhoneNumber());
        assertNotEquals(userCreateEditDto.getEmail(), actualResult.getEmail());
        assertNotEquals(userCreateEditDto.getRole().name(), actualResult.getRole().name());
        assertNotEquals(userCreateEditDto.getPassword(), actualResult.getPassword());
    }
}
