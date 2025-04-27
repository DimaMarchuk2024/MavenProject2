package com.dima.integration.mapper;

import com.dima.Enum.Role;
import com.dima.dto.UserCreateEditDto;
import com.dima.entity.User;
import com.dima.integration.annotation.IT;
import com.dima.mapper.UserCreateEditMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class UserCreateEditMapperTest {
    
    private final UserCreateEditMapper userCreateEditMapper;

    @Test
    void copySuccess() {
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

        UserCreateEditMapper.copy(userCreateEditDto, expectedResult);

        assertThat(userCreateEditDto.getFirstname()).isEqualTo(expectedResult.getFirstname());
        assertThat(userCreateEditDto.getLastname()).isEqualTo(expectedResult.getLastname());
        assertThat(userCreateEditDto.getPhoneNumber()).isEqualTo(expectedResult.getPhoneNumber());
        assertThat(userCreateEditDto.getEmail()).isEqualTo(expectedResult.getEmail());
        assertThat(userCreateEditDto.getRole().name()).isEqualTo(expectedResult.getRole().name());
        assertThat(userCreateEditDto.getPassword()).isEqualTo(expectedResult.getPassword());
    }

    @Test
    void copyFail() {
        User user = User.builder().build();
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

        UserCreateEditMapper.copy(userCreateEditDto, user);

        assertThat(user.getFirstname()).isNotEqualTo(expectedResult.getFirstname());
    }
}
