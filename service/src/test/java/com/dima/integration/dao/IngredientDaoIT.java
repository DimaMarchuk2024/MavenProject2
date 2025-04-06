package com.dima.integration.dao;

import com.dima.dao.impl.IngredientDao;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;

@IT
@RequiredArgsConstructor
class IngredientDaoIT {

    private final IngredientDao ingredientDao;
}