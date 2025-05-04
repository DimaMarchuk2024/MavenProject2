package com.dima.dto;

import lombok.Value;

@Value
public class PizzaIngredientReadDto {

    Integer id;
    PizzaReadDto pizzaReadDto;
    IngredientReadDto ingredientReadDto;
}
