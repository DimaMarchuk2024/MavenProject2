package com.dima.dao.impl;

import com.dima.entity.PizzaToOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PizzaToOrderDao  extends JpaRepository<PizzaToOrder, Long>,
                                          QuerydslPredicateExecutor<PizzaToOrder>,
                                          FilterPizzaToOrderDao {
    /**
     * Найти все заказанные пиццы по id заказа
     **/
    @Query(value = "select pto from PizzaToOrder pto join OrderDetail od on pto.id = od.pizzaToOrder.id " +
                   "where od.order.id = :orderId")
    List<PizzaToOrder> findAllByOrderId(Long orderId);

}
