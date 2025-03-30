package com.dima.integration.dao;

import com.dima.dao.impl.IngredientDao;
import com.dima.entity.Ingredient;
import com.dima.entity.Pizza;
import com.dima.integration.annotation.IT;
import com.dima.util.TestDataBuilder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@IT
@RequiredArgsConstructor
class IngredientDaoIT {

    private final IngredientDao ingredientDao;

    private final EntityManager entityManager;

    @BeforeEach
    void init() {
        TestDataBuilder.builderData(entityManager);
    }

    @Test
    void findAll() {
        List<Ingredient> actualResult = ingredientDao.findAll();

        assertThat(actualResult).hasSize(5);
        List<String> ingredientNames = actualResult.stream().map(Ingredient::getName).toList();
        assertThat(ingredientNames).containsExactlyInAnyOrder(
                "Mozzarella", "Tomato sauce", "Bacon", "Parmesan", "Onion");
    }

    @Test
    void findById() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        entityManager.flush();
        entityManager.clear();

        Optional<Ingredient> actualResult = ingredientDao.getById(ingredient.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(ingredient.getId());
    }

    @Test
    void save() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        entityManager.flush();
        entityManager.clear();

        Ingredient actualResult = entityManager.find(Ingredient.class, ingredient.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        entityManager.flush();
        entityManager.clear();
        Ingredient ingredient2 = getIngredient2(ingredient.getId());
        ingredientDao.update(ingredient2);
        entityManager.flush();
        entityManager.clear();

        Ingredient actualResult = entityManager.find(Ingredient.class, ingredient2.getId());

        assertThat(actualResult.getName()).isEqualTo(ingredient2.getName());
    }

    @Test
    void delete() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        entityManager.flush();
        ingredientDao.delete(ingredient);
        entityManager.clear();

        Pizza actualResult = entityManager.find(Pizza.class, ingredient.getId());

        assertNull(actualResult);
    }

    private Ingredient getIngredient() {
        return Ingredient.builder()
                .name("Tomatoes")
                .price(BigDecimal.valueOf(2.5))
                .build();
    }

    private Ingredient getIngredient2(Integer id) {
        return Ingredient.builder()
                .id(id)
                .name("Pineapples")
                .price(BigDecimal.valueOf(3.0))
                .build();
    }
}