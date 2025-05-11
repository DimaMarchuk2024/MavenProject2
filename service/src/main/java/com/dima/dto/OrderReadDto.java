package com.dima.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@AllArgsConstructor
@Value
public class OrderReadDto {

    Long id;
    Instant dateTime;
    BigDecimal finalPrice;
}
