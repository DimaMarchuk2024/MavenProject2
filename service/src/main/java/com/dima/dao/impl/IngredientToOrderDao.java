package com.dima.dao.impl;

import com.dima.entity.IngredientToOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface IngredientToOrderDao extends JpaRepository<IngredientToOrder, Long>,
                                              QuerydslPredicateExecutor<IngredientToOrder> {

}
