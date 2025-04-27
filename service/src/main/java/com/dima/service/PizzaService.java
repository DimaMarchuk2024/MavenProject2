package com.dima.service;

import com.dima.dao.impl.PizzaDao;
import com.dima.dto.PizzaCreateEditDto;
import com.dima.dto.PizzaReadDto;
import com.dima.mapper.PizzaCreateEditMapper;
import com.dima.mapper.PizzaReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PizzaService {

    private final PizzaDao pizzaDao;
    private final PizzaReadMapper pizzaReadMapper;
    private final PizzaCreateEditMapper pizzaCreateEditMapper;

    public Page<PizzaReadDto> findAll(Pageable pageable) {
        return pizzaDao.findAll(pageable)
                .map(pizzaReadMapper::map);
    }

    public Optional<PizzaReadDto> findById(Integer id) {
        return pizzaDao.findById(id)
                .map(pizzaReadMapper::map);
    }

    @Transactional
    public PizzaReadDto create(PizzaCreateEditDto pizzaCreateEditDto) {
        return Optional.of(pizzaCreateEditDto)
                .map(pizzaCreateEditMapper::map)
                .map(pizzaDao::save)
                .map(pizzaReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<PizzaReadDto> update(Integer id, PizzaCreateEditDto pizzaCreateEditDto) {
        return pizzaDao.findById(id)
                .map(pizza -> pizzaCreateEditMapper
                        .map(pizzaCreateEditDto, pizza))
                .map(pizzaDao::saveAndFlush)
                .map(pizzaReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return pizzaDao.findById(id)
                .map(pizza -> {
                    pizzaDao.delete(pizza);
                    pizzaDao.flush();
                    return true;
                })
                .orElse(false);
    }
}
