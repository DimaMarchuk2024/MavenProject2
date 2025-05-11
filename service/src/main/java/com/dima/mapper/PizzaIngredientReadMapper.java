package com.dima.mapper;

import com.dima.dto.IngredientReadDto;
import com.dima.dto.PizzaIngredientReadDto;
import com.dima.dto.PizzaReadDto;
import com.dima.entity.PizzaIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PizzaIngredientReadMapper implements Mapper<PizzaIngredient, PizzaIngredientReadDto> {

    private final PizzaReadMapper pizzaReadMapper;
    private final IngredientReadMapper ingredientReadMapper;

    @Override
    public PizzaIngredientReadDto map(PizzaIngredient pizzaIngredient) {
        PizzaReadDto pizzaReadDto = Optional.ofNullable(pizzaIngredient.getPizza())
                .map(pizzaReadMapper::map).orElseThrow();

        IngredientReadDto ingredientReadDto = Optional.ofNullable(pizzaIngredient.getIngredient())
                .map(ingredientReadMapper::map).orElseThrow();

        return new PizzaIngredientReadDto(
                pizzaIngredient.getId(),
                pizzaReadDto,
                ingredientReadDto
        );
    }
}