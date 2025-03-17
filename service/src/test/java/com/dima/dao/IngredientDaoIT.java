package com.dima.dao;

import com.dima.config.ApplicationConfiguration;
import com.dima.dao.impl.IngredientDao;
import com.dima.entity.Ingredient;
import com.dima.entity.Pizza;
import com.dima.util.TestDataBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class IngredientDaoIT {

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

    private final SessionFactory sessionFactory = context.getBean(SessionFactory.class);

    private final IngredientDao ingredientDao = context.getBean(IngredientDao.class);

    private Session session = (Session) context.getBean(EntityManager.class);

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataBuilder.builderData(session);
    }

    @AfterEach
    void rollback() {
        session.getTransaction().rollback();
        session.close();
        context.close();
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