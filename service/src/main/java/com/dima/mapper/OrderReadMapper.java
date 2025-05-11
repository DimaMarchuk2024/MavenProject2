package com.dima.mapper;

import com.dima.dto.OrderReadDto;
import com.dima.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    @Override
    public OrderReadDto map(Order order) {
        return new OrderReadDto(
                order.getId(),
                order.getDateTime(),
                order.getFinalPrice()
        );
    }
}
