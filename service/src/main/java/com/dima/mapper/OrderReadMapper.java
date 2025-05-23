package com.dima.mapper;

import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.dto.OrderReadDto;
import com.dima.dto.PizzaToOrderReadDto;
import com.dima.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    private final PizzaToOrderDao pizzaToOrderDao;
    private final PizzaToOrderReadMapper pizzaToOrderReadMapper;

    @Override
    public OrderReadDto map(Order order) {

        List<PizzaToOrderReadDto> pizzaToOrderReadDtos = pizzaToOrderDao.findAllByOrderId(order.getId())
                .stream()
                .map(pizzaToOrderReadMapper::map)
                .toList();

        return new OrderReadDto(
                order.getId(),
                order.getDateTime(),
                order.getFinalPrice(),
                pizzaToOrderReadDtos
        );
    }
}
