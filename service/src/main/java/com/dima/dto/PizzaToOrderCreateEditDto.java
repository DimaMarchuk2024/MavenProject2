package com.dima.dto;

import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Value
@FieldNameConstants
public class PizzaToOrderCreateEditDto {

    Integer pizzaId;

    @NotNull
    Size size;

    @NotNull
    TypeDough type;

    Integer count;

    Long userId;

    List<Integer> ingredients;
}
