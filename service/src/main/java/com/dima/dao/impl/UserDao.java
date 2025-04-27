package com.dima.dao.impl;

import com.dima.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long>, FilterUserDao, QuerydslPredicateExecutor<User> {

    /**
     Найти всех пользователей, заказавших пиццу определенного названия,
     упорядоченные сначала по имени, а потом по фамилии
     */
    @Query("select u from User u join fetch u.pizzaToOrders pto join fetch pto.pizza p where lower(p.name) like %:pizzaName% " +
           "order by u.firstname, u.lastname")
    List<User> findAllByPizzaName(String pizzaName);

    /**
     * Найти всех пользователей, сделавших заказ больше определенной суммы,
     * упорядоченные по сумме заказа (final price)
     */
    @EntityGraph(attributePaths = {"pizzaToOrders", "pizzaToOrders.orderDetails", "pizzaToOrders.orderDetails.order"})
    @Query("select u from User u join u.pizzaToOrders pto join pto.orderDetails od join od.order o " +
           "where o.finalPrice > :finalPrice order by o.finalPrice")
    List<User> findAllByOrderFinalPrice(BigDecimal finalPrice);

    Optional<User> findByEmail(String email);
}
