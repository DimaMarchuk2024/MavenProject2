package com.dima.dao.impl;

import com.dima.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OrderDetailDao extends JpaRepository<OrderDetail, Long>,
                                        FilterOrderDetailDao,
                                        QuerydslPredicateExecutor<OrderDetail> {

}
