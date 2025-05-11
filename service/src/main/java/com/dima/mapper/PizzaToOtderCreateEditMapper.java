package com.dima.mapper;

import com.dima.dao.impl.PizzaDao;
import com.dima.dao.impl.UserDao;
import com.dima.dto.PizzaToOrderInBucket;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PizzaToOtderCreateEditMapper implements Mapper<PizzaToOrderInBucket, PizzaToOrder> {

    private final PizzaDao pizzaDao;
    private final UserDao userDao;

    @Override
    public PizzaToOrder map(PizzaToOrderInBucket fromObject, PizzaToOrder toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public PizzaToOrder map(PizzaToOrderInBucket pizzaToOrderInBucket) {
        PizzaToOrder pizzaToOrder = new PizzaToOrder();
        copy(pizzaToOrderInBucket, pizzaToOrder);

        return pizzaToOrder;
    }

    private void copy(PizzaToOrderInBucket pizzaToOrderInBucket, PizzaToOrder pizzaToOrder) {
        pizzaToOrder.setPizza(getPizza(pizzaToOrderInBucket.getPizzaId()));
        pizzaToOrder.setSize(pizzaToOrderInBucket.getSize());
        pizzaToOrder.setType(pizzaToOrderInBucket.getType());
        pizzaToOrder.setCount(pizzaToOrderInBucket.getCount());
        pizzaToOrder.setPrice(pizzaToOrderInBucket.getPriceInBucket());
        pizzaToOrder.setUser(getUser(pizzaToOrderInBucket.getUserId()));
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

