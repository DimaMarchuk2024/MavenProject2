package com.dima.dao.impl;

import com.dima.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface IngredientDao extends JpaRepository<Ingredient, Integer>, QuerydslPredicateExecutor<Ingredient> {

}
