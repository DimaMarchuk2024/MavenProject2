package com.dima.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Value
@FieldNameConstants
public class IngredientCreateEditDto {

    @NotBlank
    @Size(min = 3, max = 64)
    String name;

    @Positive
    @NotNull
    @Digits(integer = 1, fraction = 2)
    BigDecimal price;
}
