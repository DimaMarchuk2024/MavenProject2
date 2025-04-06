package com.dima.dao.impl;

import com.dima.entity.PizzaIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PizzaIngredientDao extends JpaRepository<PizzaIngredient, Integer>,
                                            QuerydslPredicateExecutor<PizzaIngredient> {

}
