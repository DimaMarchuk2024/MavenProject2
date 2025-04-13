package com.dima.integration.dao;

import com.dima.dao.impl.PizzaIngredientDao;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;

@IT
@RequiredArgsConstructor
class PizzaIngredientDaoIT {

    private final PizzaIngredientDao pizzaIngredientDao;
}