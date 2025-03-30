package com.dima.integration.dao;

import com.dima.dao.impl.PizzaDao;
import com.dima.entity.Pizza;
import com.dima.integration.annotation.IT;
import com.dima.util.TestDataBuilder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@IT
@RequiredArgsConstructor
class PizzaDaoIT {

    private final PizzaDao pizzaDao;

    private final EntityManager entityManager;

    @BeforeEach
    void init() {
        TestDataBuilder.builderData(entityManager);
    }

    @Test
    void findAll() {
        List<Pizza> actualResult = pizzaDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<String> pizzaNames = actualResult.stream().map(Pizza::getName).toList();
        assertThat(pizzaNames).containsExactlyInAnyOrder("Pepperoni", "Italian", "Four cheeses");
    }

    @Test
    void findById() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        entityManager.flush();
        entityManager.clear();

        Optional<Pizza> actualResult = pizzaDao.getById(pizza.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(pizza.getId());
    }

    @Test
    void save() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        entityManager.flush();
        entityManager.clear();

        Pizza actualResult = entityManager.find(Pizza.class, pizza.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        entityManager.flush();
        entityManager.clear();
        Pizza pizza2 = getPizza2(pizza.getId());
        pizzaDao.update(pizza2);
        entityManager.flush();
        entityManager.clear();

        Pizza actualResult = entityManager.find(Pizza.class, pizza2.getId());

        assertThat(actualResult.getName()).isEqualTo(pizza2.getName());
    }

    @Test
    void delete() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        entityManager.flush();
        pizzaDao.delete(pizza);
        entityManager.clear();

        Pizza actualResult = entityManager.find(Pizza.class, pizza.getId());

        assertNull(actualResult);
    }

    private Pizza getPizza() {
        return Pizza.builder()
                .name("Vegan")
                .build();
    }
    private Pizza getPizza2(Integer id) {
        return Pizza.builder()
                .id(id)
                .name("Mexican")
                .build();
    }
}