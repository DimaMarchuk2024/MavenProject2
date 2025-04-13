package com.dima.dao.impl;

import com.dima.entity.OrderDetail;
import com.dima.filter.PizzaFilter;

import java.util.List;

public interface FilterOrderDetailDao {

    List<OrderDetail> findAllOrdersByFilter(PizzaFilter pizzaFilter);
}
