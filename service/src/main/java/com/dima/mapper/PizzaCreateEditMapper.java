package com.dima.mapper;

import com.dima.dto.PizzaCreateEditDto;
import com.dima.entity.Pizza;
import org.springframework.stereotype.Component;

@Component
public class PizzaCreateEditMapper implements Mapper<PizzaCreateEditDto, Pizza>{

    @Override
    public Pizza map(PizzaCreateEditDto fromObject, Pizza toObject) {
        copy(fromObject, toObject);

        return toObject;
    }

    @Override
    public Pizza map(PizzaCreateEditDto pizzaCreateEditDto) {
        Pizza pizza = new Pizza();
        copy(pizzaCreateEditDto, pizza);

        return pizza;
    }

    private static void copy(PizzaCreateEditDto pizzaCreateEditDto, Pizza pizza) {
        pizza.setName(pizzaCreateEditDto.getName());
    }
}
