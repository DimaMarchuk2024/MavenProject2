package com.dima.dto;

import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.List;

@Value
@FieldNameConstants
public class PizzaToOrderReadDto {

    Long id;
    PizzaReadDto pizzaReadDto;
    Size size;
    TypeDough type;
    Integer count;
    BigDecimal price;
    UserReadDto userReadDto;
    List<IngredientReadDto> ingredientReadDtos;

}
