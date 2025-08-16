package com.dima.integration.controller;

import com.dima.enumPack.Role;
import com.dima.dto.UserCreateEditDto;
import com.dima.dto.UserReadDto;
import com.dima.integration.annotation.IT;
import com.dima.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static com.dima.dto.UserCreateEditDto.Fields.birthDate;
import static com.dima.dto.UserCreateEditDto.Fields.email;
import static com.dima.dto.UserCreateEditDto.Fields.firstname;
import static com.dima.dto.UserCreateEditDto.Fields.lastname;
import static com.dima.dto.UserCreateEditDto.Fields.password;
import static com.dima.dto.UserCreateEditDto.Fields.phoneNumber;
import static com.dima.dto.UserCreateEditDto.Fields.role;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@IT
@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerIT {

    private final MockMvc mockMvc;
    private final UserService userservice;

    @Test
    void findAllByFilter() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("filter"));
    }

    @Test
    void findById() throws Exception {
        UserReadDto user = getUser();
        mockMvc.perform(get("/users/" + user.getId())
                        .queryParam("id", String.valueOf(user.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", new UserReadDto(
                        user.getId(),
                        "Sava",
                        "Savon",
                        "99-99-999",
                        "sava@gmail.com",
                        Role.USER,
                        LocalDate.of(2000, 12, 21))
                ))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("userCreate"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .param(firstname, "Sava")
                        .param(lastname, "Savon")
                        .param(phoneNumber, "99-99-999")
                        .param(email, "savasava@gmail.com")
                        .param(role, "ADMIN")
                        .param(birthDate, "2000-12-21")
                        .param(password, "999"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
        PageRequest pageable = PageRequest.of(0, 20);
        Optional<UserReadDto> actualResult = userservice.findAll(pageable)
                .stream()
                .filter(userReadDto -> userReadDto.getEmail().equals("savasava@gmail.com"))
                .findAny();

        assertTrue(actualResult.isPresent());
    }

    @Test
    void update() throws Exception {
        UserReadDto user = getUser();
        mockMvc.perform(post("/users/" + user.getId() + "/update")
                        .param(firstname, "Sava")
                        .param(lastname, "Savon")
                        .param(phoneNumber, "99-99-999")
                        .param(email, "savasava@gmail.com")
                        .param(role, "ADMIN")
                        .param(birthDate, "2000-12-21")
                        .param(password, "999")
                        .param("id", String.valueOf(user.getId())))

                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users/" + user.getId())
                );
    }

    @Test
    void delete() throws Exception {
        UserReadDto user = getUser();
        mockMvc.perform(post("/users/" + user.getId() + "/delete")
                        .queryParam("id", String.valueOf(user.getId())))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    private UserReadDto getUser() {
        return userservice.create(UserCreateEditDto.builder()
                .firstname("Sava")
                .lastname("Savon")
                .phoneNumber("99-99-999")
                .email("sava@gmail.com")
                .role(Role.USER)
                .birthDate(LocalDate.of(2000, 12, 21))
                .password("999")
                .build());
    }
}