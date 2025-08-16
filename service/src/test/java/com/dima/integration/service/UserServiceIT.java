package com.dima.integration.service;

import com.dima.enumPack.Role;
import com.dima.dto.UserCreateEditDto;
import com.dima.dto.UserReadDto;
import com.dima.entity.User;
import com.dima.filter.UserFilter;
import com.dima.integration.annotation.IT;
import com.dima.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class UserServiceIT {

    private final UserService userService;
    private final EntityManager entityManager;

    @Test
    void findAllByFilterAndPageable() {
        UserFilter filter = UserFilter.builder()
                .lastname("ov")
                .birthDate(LocalDate.of(2000, 5, 15))
                .build();
        PageRequest pageable = PageRequest.of(1, 1);

        Page<UserReadDto> result = userService.findAll(filter, pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void findAll() {
//        List<UserReadDto> result = userService.findAll();

//        assertThat(result).hasSize(3);
    }

    @Test
    void findById() {
        User buildUser = getBuildUser();
        entityManager.persist(buildUser);
        entityManager.flush();
        entityManager.clear();

        Optional<UserReadDto> result = userService.findById(buildUser.getId());

        assertTrue(result.isPresent());
        result.ifPresent(userReadDto -> assertEquals("sava@gmail.com", userReadDto.getEmail()));
    }

    @Test
    void create() {
        UserCreateEditDto userDto = getUserDto();

        UserReadDto resultUser = userService.create(userDto);

        assertEquals(userDto.getFirstname(), resultUser.getFirstname());
        assertEquals(userDto.getLastname(), resultUser.getLastname());
        assertEquals(userDto.getPhoneNumber(), resultUser.getPhoneNumber());
        assertEquals(userDto.getEmail(), resultUser.getEmail());
        assertSame(userDto.getRole(), resultUser.getRole());
        assertEquals(userDto.getBirthDate(), resultUser.getBirthDate());
    }

    @Test
    void update() {
        User buildUser = getBuildUser();
        entityManager.persist(buildUser);
        entityManager.flush();
        entityManager.clear();
        UserCreateEditDto updateUser = getUpdateUser();

        Optional<UserReadDto> resultUser = userService.update(buildUser.getId(), updateUser);
        entityManager.flush();
        entityManager.clear();

        assertTrue(resultUser.isPresent());
        resultUser.ifPresent(userReadDto -> {
            assertEquals(updateUser.getFirstname(), userReadDto.getFirstname());
            assertEquals(updateUser.getLastname(), userReadDto.getLastname());
            assertEquals(updateUser.getPhoneNumber(), userReadDto.getPhoneNumber());
            assertEquals(updateUser.getEmail(), userReadDto.getEmail());
            assertSame(updateUser.getRole(), userReadDto.getRole());
            assertEquals(updateUser.getBirthDate(), userReadDto.getBirthDate());
        });
    }

    @Test
    void delete() {
        User buildUser = getBuildUser();
        entityManager.persist(buildUser);
        entityManager.flush();

        assertTrue(userService.delete(buildUser.getId()));
        assertFalse(userService.delete(-42452L));
    }

    private static User getBuildUser() {
        return User.builder()
                .firstname("Sava")
                .lastname("Savon")
                .phoneNumber("99-99-999")
                .email("sava@gmail.com")
                .role(Role.USER)
                .birthDate(LocalDate.of(2000, 12, 21))
                .password("999")
                .build();
    }

    private static UserCreateEditDto getUserDto() {
        return UserCreateEditDto.builder()
                .firstname("Sava")
                .lastname("Savon")
                .phoneNumber("99-99-999")
                .email("sava@gmail.com")
                .role(Role.USER)
                .birthDate(LocalDate.of(2000, 12, 21))
                .password("999")
                .build();
    }

    private static UserCreateEditDto getUpdateUser() {
        return UserCreateEditDto.builder()
                .firstname("Sava1")
                .lastname("Savon1")
                .phoneNumber("99-99-000")
                .email("savaq@gmail.com")
                .role(Role.ADMIN)
                .birthDate(LocalDate.of(2001, 11, 11))
                .password("111")
                .build();
    }
}
