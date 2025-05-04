package com.dima.service;

import com.dima.dao.impl.IngredientDao;
import com.dima.dto.IngredientCreateEditDto;
import com.dima.dto.IngredientReadDto;
import com.dima.mapper.IngredientCreateEditMapper;
import com.dima.mapper.IngredientReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {

    private final IngredientDao ingredientDao;
    private final IngredientReadMapper ingredientReadMapper;
    private final IngredientCreateEditMapper ingredientCreateEditMapper;

    public Page<IngredientReadDto> findAll(Pageable pageable) {
        return ingredientDao.findAll(pageable)
                .map(ingredientReadMapper::map);
    }

    public Optional<IngredientReadDto> findById(Integer id) {
        return ingredientDao.findById(id)
                .map(ingredientReadMapper::map);
    }

    @Transactional
    public IngredientReadDto create(IngredientCreateEditDto ingredientCreateEditDto) {
        return Optional.of(ingredientCreateEditDto)
                .map(ingredientCreateEditMapper::map)
                .map(ingredientDao::save)
                .map(ingredientReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<IngredientReadDto> update(Integer id, IngredientCreateEditDto ingredientCreateEditDto) {
        return ingredientDao.findById(id)
                .map(ingredient -> ingredientCreateEditMapper
                        .map(ingredientCreateEditDto, ingredient))
                .map(ingredientDao::saveAndFlush)
                .map(ingredientReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return ingredientDao.findById(id)
                .map(ingredient -> {
                    ingredientDao.delete(ingredient);
                    ingredientDao.flush();
                    return true;
                })
                .orElse(false);
    }
}
