package com.dima.dto;

import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Value
@FieldNameConstants
public class PizzaToOrderCreateEditDto {

    Integer pizzaId;
    Size size;
    TypeDough type;
    Integer count;
    Long userId;
    List<Integer> ingredients;
}
