package com.dima.integration.dao;

import com.dima.dao.impl.PizzaIngredientDao;
import com.dima.entity.Ingredient;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaIngredient;
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
class PizzaIngredientDaoIT {

    private final PizzaIngredientDao pizzaIngredientDao;

    private final EntityManager entityManager;

    @BeforeEach
    void init() {
        TestDataBuilder.builderData(entityManager);
    }

    @Test
    void findAll() {
        List<PizzaIngredient> actualResult = pizzaIngredientDao.findAll();

        assertThat(actualResult).hasSize(8);
    }

    @Test
    void findById() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        pizzaIngredientDao.save(pizzaIngredient);
        entityManager.flush();
        entityManager.clear();

        Optional<PizzaIngredient> actualResult = pizzaIngredientDao.getById(pizzaIngredient.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(pizzaIngredient.getId());
    }

    @Test
    void save() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        pizzaIngredientDao.save(pizzaIngredient);
        entityManager.flush();
        entityManager.clear();

        PizzaIngredient actualResult = entityManager.find(PizzaIngredient.class, pizzaIngredient.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        pizzaIngredientDao.save(pizzaIngredient);
        entityManager.flush();
        entityManager.clear();
        PizzaIngredient pizzaIngredient2 = getPizzaIngredient2(pizzaIngredient.getId());
        pizzaIngredientDao.update(pizzaIngredient2);
        entityManager.flush();
        entityManager.clear();

        PizzaIngredient actualResult = entityManager.find(PizzaIngredient.class, pizzaIngredient2.getId());

        assertThat(actualResult.getPizza()).isEqualTo(pizzaIngredient2.getPizza());
        assertThat(actualResult.getIngredient()).isEqualTo(pizzaIngredient2.getIngredient());
    }

    @Test
    void delete() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        pizzaIngredientDao.save(pizzaIngredient);
        entityManager.flush();
        pizzaIngredientDao.delete(pizzaIngredient);
        entityManager.clear();

        PizzaIngredient actualResult = entityManager.find(PizzaIngredient.class, pizzaIngredient.getId());

        assertNull(actualResult);
    }

    private PizzaIngredient getPizzaIngredient() {
        Pizza pizza = getPizza();
        Ingredient ingredient = getIngredient();

        return PizzaIngredient.builder()
                .pizza(pizza)
                .ingredient(ingredient)
                .build();
    }

    private Pizza getPizza() {
        Pizza pizza = Pizza.builder()
                .name("Vegan")
                .build();
        entityManager.persist(pizza);

        return pizza;
    }

    private Ingredient getIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .name("Tomatoes")
                .price(BigDecimal.valueOf(2.5).setScale(2))
                .build();
        entityManager.persist(ingredient);

        return ingredient;
    }

    private PizzaIngredient getPizzaIngredient2(Integer id) {
        Pizza pizza2 = getPizza2();
        Ingredient ingredient2 = getIngredient2();

        return PizzaIngredient.builder()
                .id(id)
                .pizza(pizza2)
                .ingredient(ingredient2)
                .build();
    }

    private Pizza getPizza2() {
        Pizza pizza2 = Pizza.builder()
                .name("Mexican")
                .build();
        entityManager.persist(pizza2);

        return pizza2;
    }

    private Ingredient getIngredient2() {
        Ingredient ingredient2 = Ingredient.builder()
                .name("Pineapples")
                .price(BigDecimal.valueOf(3.0).setScale(2))
                .build();
        entityManager.persist(ingredient2);

        return ingredient2;
    }
}