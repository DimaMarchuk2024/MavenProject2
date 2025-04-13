package com.dima.integration.dao;

import com.dima.dao.impl.OrderDetailDao;
import com.dima.entity.Order;
import com.dima.entity.OrderDetail;
import com.dima.filter.PizzaFilter;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
class OrderDetailDaoIT {

    private final OrderDetailDao orderDetailDao;

    @Test
    void findAllOrdersByFilter() {
        PizzaFilter pizzaFilter = PizzaFilter.builder()
                .pizzaName("Italian")
                .build();

        List<OrderDetail> actualResult = orderDetailDao.findAllOrdersByFilter(pizzaFilter);

        assertThat(actualResult).hasSize(2);
        List<Order> orders = actualResult.stream().map(OrderDetail::getOrder).toList();
        List<BigDecimal> finalPrices = orders.stream().map(Order::getFinalPrice).toList();
        assertThat(finalPrices).contains(BigDecimal.valueOf(85).setScale(2),BigDecimal.valueOf(95).setScale(2));
    }
}