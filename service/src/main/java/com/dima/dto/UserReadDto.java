package com.dima.dto;

import com.dima.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Value
public class UserReadDto {

    Long id;
    String firstname;
    String lastname;
    String phoneNumber;
    String email;
    Role role;
    LocalDate birthDate;
}
