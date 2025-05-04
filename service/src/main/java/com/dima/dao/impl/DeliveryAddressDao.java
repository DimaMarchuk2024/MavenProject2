package com.dima.dao.impl;

import com.dima.entity.DeliveryAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface DeliveryAddressDao extends JpaRepository<DeliveryAddress, Long>,
                                            FilterDeliveryAddressDao,
                                            QuerydslPredicateExecutor<DeliveryAddress> {

    /**
     * Найти все адреса доставки по фрагменту названия адреса без учета регистра,
     * упорядоченные по названию адресов доставки.
     **/
    @Query(value = "select d from DeliveryAddress d " +
                   "where lower(d.address) like %:address% order by d.address")
    Page<DeliveryAddress> findAllBy(String address, Pageable pageable);

    /**
     * Найти все адреса доставки по id пользователя,
     * упорядоченные по названию адресов доставки.
     **/
    @Query(value = "select d from DeliveryAddress d " +
                   "where d.user.id = :userId order by d.address")
    List<DeliveryAddress> findAllByUserId(Long userId);
}
