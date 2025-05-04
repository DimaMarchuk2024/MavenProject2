package com.dima.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Value
@FieldNameConstants
public class IngredientCreateEditDto {

    String name;
    BigDecimal price;
}
