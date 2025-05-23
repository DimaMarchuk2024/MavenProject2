package com.dima.mapper;

import com.dima.dao.impl.IngredientToOrderDao;
import com.dima.dto.IngredientReadDto;
import com.dima.dto.PizzaReadDto;
import com.dima.dto.PizzaToOrderReadDto;
import com.dima.dto.UserReadDto;
import com.dima.entity.IngredientToOrder;
import com.dima.entity.PizzaToOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PizzaToOrderReadMapper implements Mapper<PizzaToOrder, PizzaToOrderReadDto> {

    private final UserReadMapper userReadMapper;
    private final PizzaReadMapper pizzaReadMapper;
    private final IngredientToOrderDao ingredientToOrderDao;
    private final IngredientReadMapper ingredientReadMapper;

    @Override
    public PizzaToOrderReadDto map(PizzaToOrder pizzaToOrder) {
        PizzaReadDto pizzaReadDto = Optional.ofNullable(pizzaToOrder.getPizza())
                .map(pizzaReadMapper::map).orElseThrow();

        UserReadDto userReadDto = Optional.ofNullable(pizzaToOrder.getUser())
                .map(userReadMapper::map).orElseThrow();

        List<IngredientReadDto> ingredientReadDtos = ingredientToOrderDao.findAllByPizzaToOrderId(pizzaToOrder.getId())
                .stream()
                .map(IngredientToOrder::getIngredient)
                .map(ingredientReadMapper::map)
                .toList();

        return new PizzaToOrderReadDto(
                pizzaToOrder.getId(),
                pizzaReadDto,
                pizzaToOrder.getSize(),
                pizzaToOrder.getType(),
                pizzaToOrder.getCount(),
                pizzaToOrder.getPrice(),
                userReadDto,
                ingredientReadDtos
        );
    }
}