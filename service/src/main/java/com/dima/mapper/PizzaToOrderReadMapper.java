package com.dima.mapper;

import com.dima.dto.PizzaReadDto;
import com.dima.dto.PizzaToOrderReadDto;
import com.dima.dto.UserReadDto;
import com.dima.entity.PizzaToOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PizzaToOrderReadMapper implements Mapper<PizzaToOrder, PizzaToOrderReadDto> {

    private final UserReadMapper userReadMapper;
    private final PizzaReadMapper pizzaReadMapper;

    @Override
    public PizzaToOrderReadDto map(PizzaToOrder pizzaToOrder) {
        PizzaReadDto pizzaReadDto = Optional.ofNullable(pizzaToOrder.getPizza())
                .map(pizzaReadMapper::map).orElseThrow();

        UserReadDto userReadDto = Optional.ofNullable(pizzaToOrder.getUser())
                .map(userReadMapper::map).orElseThrow();

        return new PizzaToOrderReadDto(
                pizzaToOrder.getId(),
                pizzaReadDto,
                pizzaToOrder.getSize(),
                pizzaToOrder.getType(),
                pizzaToOrder.getCount(),
                userReadDto
        );
    }
}