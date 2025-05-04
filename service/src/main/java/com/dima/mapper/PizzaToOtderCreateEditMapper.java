package com.dima.mapper;

import com.dima.dao.impl.PizzaDao;
import com.dima.dao.impl.UserDao;
import com.dima.dto.PizzaToOrderCreateEditDto;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PizzaToOtderCreateEditMapper implements Mapper<PizzaToOrderCreateEditDto, PizzaToOrder> {

    private final PizzaDao pizzaDao;
    private final UserDao userDao;

    @Override
    public PizzaToOrder map(PizzaToOrderCreateEditDto fromObject, PizzaToOrder toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public PizzaToOrder map(PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto) {
        PizzaToOrder pizzaToOrder = new PizzaToOrder();
        copy(pizzaToOrderCreateEditDto, pizzaToOrder);

        return pizzaToOrder;
    }

    private void copy(PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto, PizzaToOrder pizzaToOrder) {
        pizzaToOrder.setPizza(getPizza(pizzaToOrderCreateEditDto.getPizzaId()));
        pizzaToOrder.setSize(pizzaToOrderCreateEditDto.getSize());
        pizzaToOrder.setType(pizzaToOrderCreateEditDto.getType());
        pizzaToOrder.setCount(pizzaToOrderCreateEditDto.getCount());
        pizzaToOrder.setUser(getUser(pizzaToOrderCreateEditDto.getUserId()));
    }

    private Pizza getPizza(Integer pizzaId) {
        return Optional.ofNullable(pizzaId)
                .flatMap(pizzaDao::findById)
                .orElseThrow();
    }

    private User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userDao::findById)
                .orElseThrow();
    }
}

