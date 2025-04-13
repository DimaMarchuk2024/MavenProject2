package com.dima.integration.dao;

import com.dima.dao.impl.PizzaDao;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;

@IT
@RequiredArgsConstructor
class PizzaDaoIT {

    private final PizzaDao pizzaDao;
}