package com.dima.entity;

import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PizzaIT {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void save() {
        Pizza pizza = getPizza();

        session.persist(pizza);
        Pizza actualResult = session.get(Pizza.class, pizza.getId());

        assertNotNull(actualResult.getId());
    }


    @Test
    void get() {
        Pizza pizza = getPizza();
        session.persist(pizza);

        Pizza actualResult = session.get(Pizza.class, pizza.getId());

        assertThat(actualResult.getName()).isEqualTo(pizza.getName());
    }

    @Test
    void update() {
        Pizza pizza = getPizza();
        session.persist(pizza);
        Pizza pizza2 = Pizza.builder()
                .id(pizza.getId())
                .name("Pepperoni2")
                .build();

        session.merge(pizza2);
        Pizza actualResult = session.get(Pizza.class, pizza.getId());

        assertThat(actualResult.getName()).isEqualTo(pizza2.getName());
    }

    @Test
    void delete() {
        Pizza pizza = getPizza();
        session.persist(pizza);

        session.remove(pizza);
        Pizza actualResult = session.get(Pizza.class, pizza.getId());

        assertNull(actualResult);
    }

    private static Pizza getPizza() {
        return Pizza.builder()
                .name("Pepperoni")
                .build();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
        sessionFactory.close();
    }
}