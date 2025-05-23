package com.dima.service;

import com.dima.dao.impl.OrderDao;
import com.dima.dto.OrderReadDto;
import com.dima.mapper.OrderReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderReadMapper orderReadMapper;

    public Page<OrderReadDto> findAll(Pageable pageable) {
        return orderDao.findAll(pageable)
                .map(orderReadMapper::map);
    }

    public List<OrderReadDto> findAllByUserId(Long userId) {
        return orderDao.findAllByUserId(userId).stream()
                .map(orderReadMapper::map)
                .toList();
    }

    public Optional<OrderReadDto> findById(Long id) {
        return orderDao.findById(id)
                .map(orderReadMapper::map);
    }
}

