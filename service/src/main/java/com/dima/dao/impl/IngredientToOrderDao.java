package com.dima.dao.impl;

import com.dima.entity.IngredientToOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface IngredientToOrderDao extends JpaRepository<IngredientToOrder, Long>,
                                              QuerydslPredicateExecutor<IngredientToOrder> {

    /**
     * Найти все ингредиенты заказанной пиццы по id заказанной пиццы
     **/
    @Query(value = "select ito from IngredientToOrder ito " +
                   "where ito.pizzaToOrder.id = :id")
    List<IngredientToOrder> findAllByPizzaToOrderId(Long id);
}
