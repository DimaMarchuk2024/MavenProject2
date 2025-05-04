package com.dima.dto;

import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class PizzaToOrderReadDto {

    Long id;
    PizzaReadDto pizzaReadDto;
    Size size;
    TypeDough type;
    Integer count;
    UserReadDto userReadDto;

//    List<IngredientReadDto> ingredientReadDtos;
//    это поле необходимо для нахождение из базы всех ингредиентов заказанной пиццы?
}
