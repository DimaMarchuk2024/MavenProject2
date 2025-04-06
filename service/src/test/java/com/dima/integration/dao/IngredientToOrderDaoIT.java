package com.dima.integration.dao;

import com.dima.dao.impl.IngredientToOrderDao;

import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;

@IT
@RequiredArgsConstructor
class IngredientToOrderDaoIT {

    private final IngredientToOrderDao ingredientToOrderDao;
}