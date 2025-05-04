package com.dima.integration.controller;

import com.dima.enumPack.Role;
import com.dima.dto.PageResponse;
import com.dima.dto.UserReadDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    @Test
    void ofSuccess() {
        Page<UserReadDto> page = new PageImpl<>(List.of(new UserReadDto(
                3L,
                "Dima",
                "Dimov",
                "33-33-333",
                "dima@gmail.com",
                Role.USER,
                LocalDate.of(1990, 11, 28))));

        PageResponse<UserReadDto> pageResponse = PageResponse.of(page);

        assertThat(pageResponse.getContent()).isNotEmpty();
        assertThat(pageResponse.getContent().size()).isEqualTo(1);
        assertThat(pageResponse.getContent().get(0).getPhoneNumber()).isEqualTo("33-33-333");
        assertThat(pageResponse.getMetadata()).isNotNull();
        assertThat(pageResponse.getMetadata().getSize()).isEqualTo(1);
        assertThat(pageResponse.getMetadata().getTotalElement()).isEqualTo(1);
    }
}