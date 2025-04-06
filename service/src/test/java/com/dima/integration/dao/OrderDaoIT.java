package com.dima.integration.dao;

import com.dima.dao.impl.OrderDao;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;

@IT
@RequiredArgsConstructor
class OrderDaoIT {

    private final OrderDao orderDao;
}