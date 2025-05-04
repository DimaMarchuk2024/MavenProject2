package com.dima.mapper;

import com.dima.dto.PizzaCreateEditDto;
import com.dima.entity.Pizza;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;

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

        Optional.ofNullable(pizzaCreateEditDto.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image -> pizza.setImage(image.getOriginalFilename()));
    }
}
