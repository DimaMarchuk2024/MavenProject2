package com.dima.mapper;

import com.dima.dto.IngredientReadDto;
import com.dima.entity.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientReadMapper implements Mapper<Ingredient, IngredientReadDto> {

    @Override
    public IngredientReadDto map(Ingredient ingredient) {

        return new IngredientReadDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getPrice()
        );
    }
}