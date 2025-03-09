package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.DeliveryAddress;
import org.hibernate.Session;

public class DeliveryAddressDao extends DaoBase<Long, DeliveryAddress> {
    public DeliveryAddressDao(Session session) {
        super(DeliveryAddress.class, session);
    }
}
