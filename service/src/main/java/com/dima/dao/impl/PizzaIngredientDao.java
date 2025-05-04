package com.dima.dao.impl;

import com.dima.entity.PizzaIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PizzaIngredientDao extends JpaRepository<PizzaIngredient, Integer>,
                                            QuerydslPredicateExecutor<PizzaIngredient> {
    /**
     * Найти все ингредиенты по id пиццы.
     **/
    @Query(value = "select p from PizzaIngredient p " +
                   "where p.pizza.id = :pizzaId")
    List<PizzaIngredient> findAllByPizzaId(Integer pizzaId);
}
