package com.dima.entity;

import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class IngredientIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void save() {
        Ingredient ingredient = getIngredient();
        session.persist(ingredient);
        session.flush();
        session.evict(ingredient);

        Ingredient actualResult = session.get(Ingredient.class, ingredient.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        Ingredient ingredient = getIngredient();
        session.persist(ingredient);
        session.flush();
        session.evict(ingredient);

        Ingredient actualResult = session.get(Ingredient.class, ingredient.getId());

        assertThat(actualResult.getName()).isEqualTo(ingredient.getName());
        assertThat(actualResult.getPrice()).isEqualTo(ingredient.getPrice());
    }

    @Test
    void update() {
        Ingredient ingredient = getIngredient();
        session.persist(ingredient);
        session.evict(ingredient);
        Ingredient ingredient2 = getIngredient2(ingredient);
        session.merge(ingredient2);
        session.flush();
        session.evict(ingredient2);

        Ingredient actualResult = session.get(Ingredient.class, ingredient.getId());

        assertThat(actualResult.getName()).isEqualTo(ingredient2.getName());
        assertThat(actualResult.getPrice()).isEqualTo(ingredient2.getPrice());
    }

    @Test
    void delete() {
        Ingredient ingredient = getIngredient();
        session.persist(ingredient);
        session.remove(ingredient);
        session.flush();
        session.evict(ingredient);

        Ingredient actualResult = session.get(Ingredient.class, ingredient.getId());

        assertNull(actualResult);
    }

    private static Ingredient getIngredient2(Ingredient ingredient) {
        return Ingredient.builder()
                .id(ingredient.getId())
                .name("Mozzarella2")
                .price(BigDecimal.valueOf(3.9))
                .build();
    }

    private static Ingredient getIngredient() {
        return Ingredient.builder()
                .name("Mozzarella")
                .price(BigDecimal.valueOf(3.5).setScale(2))
                .build();
    }
}