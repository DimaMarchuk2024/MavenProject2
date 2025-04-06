package com.dima.dao.impl;

import com.dima.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OrderDao extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

}
