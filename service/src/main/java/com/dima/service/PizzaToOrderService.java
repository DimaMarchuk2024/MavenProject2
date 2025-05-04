package com.dima.service;

import com.dima.dao.impl.IngredientDao;
import com.dima.dao.impl.IngredientToOrderDao;
import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.dto.PizzaToOrderCreateEditDto;
import com.dima.dto.PizzaToOrderReadDto;
import com.dima.entity.IngredientToOrder;
import com.dima.mapper.PizzaToOrderReadMapper;
import com.dima.mapper.PizzaToOtderCreateEditMapper;
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
public class PizzaToOrderService {

    private final PizzaToOrderDao pizzaToOrderDao;
    private final PizzaToOrderReadMapper pizzaToOrderReadMapper;
    private final PizzaToOtderCreateEditMapper pizzaToOtderCreateEditMapper;
    private final IngredientToOrderDao ingredientToOrderDao;
    private final IngredientDao ingredientDao;

    public Page<PizzaToOrderReadDto> findAll(Pageable pageable) {
        return pizzaToOrderDao.findAll(pageable)
                .map(pizzaToOrderReadMapper::map);
    }

    public Optional<PizzaToOrderReadDto> findById(Long id) {
        return pizzaToOrderDao.findById(id)
                .map(pizzaToOrderReadMapper::map);
    }

    @Transactional
    public PizzaToOrderReadDto create(PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto) {
        PizzaToOrderReadDto pizzaToOrderReadDto = Optional.of(pizzaToOrderCreateEditDto)
                .map(pizzaToOtderCreateEditMapper::map)
                .map(pizzaToOrderDao::save)
                .map(pizzaToOrderReadMapper::map)
                .orElseThrow();

        Long pizzaToOrderId = pizzaToOrderReadDto.getId();
        List<Integer> ingredients = pizzaToOrderCreateEditDto.getIngredients();
        for(Integer ingredient : ingredients) {
            IngredientToOrder ingredientToOrder = IngredientToOrder.builder()
                    .pizzaToOrder(pizzaToOrderDao.findById(pizzaToOrderId).orElseThrow())
                    .ingredient(ingredientDao.findById(ingredient).orElseThrow())
                    .build();
            ingredientToOrderDao.save(ingredientToOrder);
        }

        return pizzaToOrderReadDto;
    }

    @Transactional
    public Optional<PizzaToOrderReadDto> update(Long id, PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto) {
        Optional<PizzaToOrderReadDto> pizzaToOrderReadDto = pizzaToOrderDao.findById(id)
                .map(pizzaToOrder -> pizzaToOtderCreateEditMapper.map(pizzaToOrderCreateEditDto, pizzaToOrder))
                .map(pizzaToOrderDao::saveAndFlush)
                .map(pizzaToOrderReadMapper::map);

        Long pizzaToOrderId = pizzaToOrderReadDto.get().getId();
        List<Integer> ingredients = pizzaToOrderCreateEditDto.getIngredients();
        for(Integer ingredient : ingredients) {
            IngredientToOrder ingredientToOrder = IngredientToOrder.builder()
                    .pizzaToOrder(pizzaToOrderDao.findById(pizzaToOrderId).orElseThrow())
                    .ingredient(ingredientDao.findById(ingredient).orElseThrow())
                    .build();
            ingredientToOrderDao.saveAndFlush(ingredientToOrder);
        }

        return pizzaToOrderReadDto;
    }

    @Transactional
    public boolean delete(Long id) {
        return pizzaToOrderDao.findById(id)
                .map(pizzaToOrder -> {
                    pizzaToOrderDao.delete(pizzaToOrder);
                    pizzaToOrderDao.flush();
                    return true;
                })
                .orElse(false);
    }
}

