package com.dima.dao.impl;

import com.dima.entity.PizzaToOrder;
import com.dima.filter.PizzaFilter;
import com.dima.filter.UserFilter;

import java.util.List;

public interface FilterPizzaToOrderDao {

    List<PizzaToOrder> findAllByUserFilter(UserFilter userFilter);

    List<PizzaToOrder> findAllByPizzaFilter(PizzaFilter pizzaFilter);
}
