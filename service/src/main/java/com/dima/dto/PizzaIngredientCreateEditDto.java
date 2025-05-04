package com.dima.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class PizzaIngredientCreateEditDto {

    Integer pizzaId;
    Integer ingredientId;
}
