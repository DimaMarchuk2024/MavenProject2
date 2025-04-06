package com.dima.dao.impl;

import com.dima.entity.DeliveryAddress;
import com.dima.filter.UserFilter;

import java.util.List;

public interface FilterDeliveryAddressDao {

    List<DeliveryAddress> findAllByUserFilter(UserFilter userFilter);
}
