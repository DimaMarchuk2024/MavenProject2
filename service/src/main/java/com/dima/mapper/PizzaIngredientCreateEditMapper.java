package com.dima.mapper;

import com.dima.dao.impl.IngredientDao;
import com.dima.dao.impl.PizzaDao;
import com.dima.dto.PizzaIngredientCreateEditDto;
import com.dima.entity.Ingredient;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PizzaIngredientCreateEditMapper implements Mapper<PizzaIngredientCreateEditDto, PizzaIngredient> {

    private final PizzaDao pizzaDao;
    private final IngredientDao ingredientDao;

    @Override
    public PizzaIngredient map(PizzaIngredientCreateEditDto fromObject, PizzaIngredient toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public PizzaIngredient map(PizzaIngredientCreateEditDto pizzaIngredientCreateEditDto) {
        PizzaIngredient pizzaIngredient = new PizzaIngredient();
        copy(pizzaIngredientCreateEditDto, pizzaIngredient);

        return pizzaIngredient;
    }

    private void copy(PizzaIngredientCreateEditDto pizzaIngredientCreateEditDto, PizzaIngredient pizzaIngredient) {
        pizzaIngredient.setPizza(getPizza(pizzaIngredientCreateEditDto.getPizzaId()));
        pizzaIngredient.setIngredient(getIngredient(pizzaIngredientCreateEditDto.getIngredientId()));
    }

    private Pizza getPizza(Integer pizzaId) {
        return Optional.ofNullable(pizzaId)
                .flatMap(pizzaDao::findById)
                .orElseThrow();
    }

    private Ingredient getIngredient(Integer ingredientId) {
        return Optional.ofNullable(ingredientId)
                .flatMap(ingredientDao::findById)
                .orElseThrow();
    }
}

