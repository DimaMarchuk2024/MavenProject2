package com.dima.dto;

import com.dima.Enum.Role;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Builder
@Value
@FieldNameConstants
public class UserCreateEditDto {

    String firstname;
    String lastname;
    String phoneNumber;
    String email;
    Role role;
    LocalDate birthDate;
    String password;
}
