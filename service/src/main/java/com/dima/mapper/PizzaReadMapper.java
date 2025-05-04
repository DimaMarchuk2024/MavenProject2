package com.dima.mapper;

import com.dima.dto.PizzaReadDto;
import com.dima.entity.Pizza;
import org.springframework.stereotype.Component;

@Component
public class PizzaReadMapper implements Mapper<Pizza, PizzaReadDto> {

    @Override
    public PizzaReadDto map(Pizza pizza) {
        return new PizzaReadDto(
                pizza.getId(),
                pizza.getName(),
                pizza.getImage()
        );
    }
}
