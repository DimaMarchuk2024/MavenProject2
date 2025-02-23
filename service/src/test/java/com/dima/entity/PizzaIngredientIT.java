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

class PizzaIngredientIT {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void save() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();

        session.persist(pizzaIngredient);
        PizzaIngredient actualResult = session.get(PizzaIngredient.class, pizzaIngredient.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        session.persist(pizzaIngredient);

        PizzaIngredient actualResult = session.get(PizzaIngredient.class, pizzaIngredient.getId());

        assertThat(actualResult.getPizza()).isEqualTo(pizzaIngredient.getPizza());
        assertThat(actualResult.getIngredient()).isEqualTo(pizzaIngredient.getIngredient());
    }

    @Test
    void update() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        session.persist(pizzaIngredient);
        Ingredient ingredient2 = Ingredient.builder()
                .name("Mozzarella2")
                .price(BigDecimal.valueOf(3.9))
                .build();
        session.persist(ingredient2);
        Pizza pizza2 = Pizza.builder()
                .name("Pepperoni2")
                .build();
        session.persist(pizza2);
        PizzaIngredient pizzaIngredient2 = PizzaIngredient.builder()
                .id(pizzaIngredient.getId())
                .pizza(pizza2)
                .ingredient(ingredient2)
                .build();

        session.merge(pizzaIngredient2);
        PizzaIngredient actualResult = session.get(PizzaIngredient.class, pizzaIngredient.getId());

        assertThat(actualResult.getPizza()).isEqualTo(pizzaIngredient2.getPizza());
        assertThat(actualResult.getIngredient()).isEqualTo(pizzaIngredient2.getIngredient());
    }

    @Test
    void delete() {
        PizzaIngredient pizzaIngredient = getPizzaIngredient();
        session.persist(pizzaIngredient);

        session.remove(pizzaIngredient);
        PizzaIngredient actualResult = session.get(PizzaIngredient.class, pizzaIngredient.getId());

        assertNull(actualResult);
    }

    private PizzaIngredient getPizzaIngredient() {
        Pizza pizza = Pizza.builder()
                .name("Pepperoni")
                .build();
        session.persist(pizza);

        Ingredient ingredient = Ingredient.builder()
                .name("Mozzarella")
                .price(BigDecimal.valueOf(3.5))
                .build();
        session.persist(ingredient);

        return PizzaIngredient.builder()
                .pizza(pizza)
                .ingredient(ingredient)
                .build();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
        sessionFactory.close();
    }
}