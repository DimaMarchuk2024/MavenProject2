package com.dima.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class IngredientReadDto {

    Integer id;
    String name;
    BigDecimal price;
}
