package com.dima.dao.impl;

import com.dima.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

    /**
     * Найти все заказы по id пользователя,
     * упорядоченные по дате заказа по убыванию.
     **/

    @Query(value = "select o from Order o " +
                   "inner join o.orderDetails od on o.id = od.order.id " +
                   "inner join od.pizzaToOrder pto on od.pizzaToOrder.id = pto.id " +
                   "where pto.user.id = :userId order by o.dateTime desc")

    List<Order> findAllByUserId(Long userId);
}