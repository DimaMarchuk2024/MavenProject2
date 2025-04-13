package com.dima.dao.impl;

import com.dima.entity.PizzaToOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PizzaToOrderDao  extends JpaRepository<PizzaToOrder, Long>,
                                          QuerydslPredicateExecutor<PizzaToOrder>,
                                          FilterPizzaToOrderDao {

}
