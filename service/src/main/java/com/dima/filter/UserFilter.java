package com.dima.filter;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserFilter {

    String firstname;
    String lastname;
    LocalDate birthDate;
    String phoneNumber;
    String email;
}
