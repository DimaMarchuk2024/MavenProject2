package com.dima.dto;

import com.dima.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Builder
@Value
@FieldNameConstants
public class UserCreateEditDto {

    @NotBlank
    @Size(min = 2, max = 64)
    String firstname;

    @NotBlank
    @Size(min = 2, max = 64)
    String lastname;

    @NotBlank
    String phoneNumber;

    @Email
    String email;

    @NotNull
    Role role;

    @NotNull
    @Past
    LocalDate birthDate;

    @NotBlank
    @Size(min = 3, max = 64)
    String password;
}
