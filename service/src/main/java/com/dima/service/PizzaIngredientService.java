package com.dima.service;

import com.dima.dao.impl.PizzaIngredientDao;
import com.dima.dto.PizzaIngredientCreateEditDto;
import com.dima.dto.PizzaIngredientReadDto;
import com.dima.mapper.PizzaIngredientCreateEditMapper;
import com.dima.mapper.PizzaIngredientReadMapper;
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
public class PizzaIngredientService {

    private final PizzaIngredientDao pizzaIngredientDao;
    private final PizzaIngredientReadMapper pizzaIngredientReadMapper;
    private final PizzaIngredientCreateEditMapper pizzaIngredientCreateEditMapper;

    public Page<PizzaIngredientReadDto> findAll(Pageable pageable) {
        return pizzaIngredientDao.findAll(pageable)
                .map(pizzaIngredientReadMapper::map);
    }

    public List<PizzaIngredientReadDto> findAllByPizzaId(Integer pizzaId) {
        return pizzaIngredientDao.findAllByPizzaId(pizzaId).stream()
                .map(pizzaIngredientReadMapper::map)
                .toList();
    }

    public Optional<PizzaIngredientReadDto> findById(Integer id) {
        return pizzaIngredientDao.findById(id)
                .map(pizzaIngredientReadMapper::map);
    }

    @Transactional
    public PizzaIngredientReadDto create(PizzaIngredientCreateEditDto pizzaIngredientCreateEditDto) {
        return Optional.of(pizzaIngredientCreateEditDto)
                .map(pizzaIngredientCreateEditMapper::map)
                .map(pizzaIngredientDao::save)
                .map(pizzaIngredientReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<PizzaIngredientReadDto> update(Integer id, PizzaIngredientCreateEditDto pizzaIngredientCreateEditDto) {
        return pizzaIngredientDao.findById(id)
                .map(pizzaIngredient -> pizzaIngredientCreateEditMapper.map(pizzaIngredientCreateEditDto, pizzaIngredient))
                .map(pizzaIngredientDao::saveAndFlush)
                .map(pizzaIngredientReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return pizzaIngredientDao.findById(id)
                .map(pizzaIngredient -> {
                    pizzaIngredientDao.delete(pizzaIngredient);
                    pizzaIngredientDao.flush();
                    return true;
                })
                .orElse(false);
    }
}

