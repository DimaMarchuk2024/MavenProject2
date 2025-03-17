package com.dima.dao;

import com.dima.dao.impl.IngredientDao;
import com.dima.entity.Ingredient;
import com.dima.entity.Pizza;
import com.dima.util.HibernateUtil;
import com.dima.util.TestDataBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IngredientDaoIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private Session session;
    private IngredientDao ingredientDao;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        ingredientDao = new IngredientDao(session);
        session.beginTransaction();
        TestDataBuilder.builderData(session);
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
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
        session.flush();
        session.clear();

        Optional<Ingredient> actualResult = ingredientDao.getById(ingredient.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(ingredient.getId());
    }

    @Test
    void save() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        session.flush();
        session.clear();

        Ingredient actualResult = session.get(Ingredient.class, ingredient.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        session.flush();
        session.clear();
        Ingredient ingredient2 = getIngredient2(ingredient.getId());
        ingredientDao.update(ingredient2);
        session.flush();
        session.clear();

        Ingredient actualResult = session.get(Ingredient.class, ingredient2.getId());

        assertThat(actualResult.getName()).isEqualTo(ingredient2.getName());
    }

    @Test
    void delete() {
        Ingredient ingredient = getIngredient();
        ingredientDao.save(ingredient);
        session.flush();
        session.clear();
        ingredientDao.delete(ingredient);
        session.clear();

        Pizza actualResult = session.get(Pizza.class, ingredient.getId());

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