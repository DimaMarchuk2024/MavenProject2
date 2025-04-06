package com.dima.dao.impl;

import com.dima.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PizzaDao extends JpaRepository<Pizza, Integer>, QuerydslPredicateExecutor<Pizza> {

}
