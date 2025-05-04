package com.dima.mapper;

import com.dima.dto.IngredientCreateEditDto;
import com.dima.entity.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientCreateEditMapper implements Mapper<IngredientCreateEditDto, Ingredient> {

    @Override
    public Ingredient map(IngredientCreateEditDto fromObject, Ingredient toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Ingredient map(IngredientCreateEditDto ingredientCreateEditDto) {
        Ingredient ingredient = new Ingredient();
        copy(ingredientCreateEditDto, ingredient);

        return ingredient;
    }

    private void copy(IngredientCreateEditDto ingredientCreateEditDto, Ingredient ingredient) {
        ingredient.setName(ingredientCreateEditDto.getName());
        ingredient.setPrice(ingredientCreateEditDto.getPrice());
    }
}

