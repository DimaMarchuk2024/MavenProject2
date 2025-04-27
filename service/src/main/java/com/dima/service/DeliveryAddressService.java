package com.dima.service;

import com.dima.dao.impl.DeliveryAddressDao;
import com.dima.dto.DeliveryAddressCreateEditDto;
import com.dima.dto.DeliveryAddressReadDto;
import com.dima.mapper.DeliveryAddressCreateEditMapper;
import com.dima.mapper.DeliveryAddressReadMapper;
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
public class DeliveryAddressService {

    private final DeliveryAddressDao deliveryAddressDao;
    private final DeliveryAddressReadMapper deliveryAddressReadMapper;
    private final DeliveryAddressCreateEditMapper deliveryAddressCreateEditMapper;

    public Page<DeliveryAddressReadDto> findAllBy(String address, Pageable pageable) {
        return deliveryAddressDao.findAllBy(address, pageable)
                .map(deliveryAddressReadMapper::map);
    }

    public List<DeliveryAddressReadDto> findAll() {
        return deliveryAddressDao.findAll()
                .stream()
                .map(deliveryAddressReadMapper::map)
                .toList();
    }

    public Optional<DeliveryAddressReadDto> findById(Long id) {
        return deliveryAddressDao.findById(id)
                .map(deliveryAddressReadMapper::map);
    }

    @Transactional
    public DeliveryAddressReadDto create(DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        return Optional.of(deliveryAddressCreateEditDto)
                .map(deliveryAddressCreateEditMapper::map)
                .map(deliveryAddressDao::save)
                .map(deliveryAddressReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<DeliveryAddressReadDto> update(Long id, DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        return deliveryAddressDao.findById(id)
                .map(deliveryAddress -> deliveryAddressCreateEditMapper.map(deliveryAddressCreateEditDto, deliveryAddress))
                .map(deliveryAddressDao::saveAndFlush)
                .map(deliveryAddressReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return deliveryAddressDao.findById(id)
                .map(deliveryAddress -> {
                    deliveryAddressDao.delete(deliveryAddress);
                    deliveryAddressDao.flush();
                    return true;
                })
                .orElse(false);
    }
}

