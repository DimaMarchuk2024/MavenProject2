package com.dima.dao;

import com.dima.dao.impl.PizzaDao;
import com.dima.entity.Pizza;
import com.dima.util.HibernateUtil;
import com.dima.util.TestDataBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PizzaDaoIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private Session session;
    private PizzaDao pizzaDao;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        pizzaDao = new PizzaDao(session);
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
        List<Pizza> actualResult = pizzaDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<String> pizzaNames = actualResult.stream().map(Pizza::getName).toList();
        assertThat(pizzaNames).containsExactlyInAnyOrder("Pepperoni", "Italian", "Four cheeses");
    }

    @Test
    void findById() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        session.flush();
        session.clear();

        Optional<Pizza> actualResult = pizzaDao.getById(pizza.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(pizza.getId());
    }

    @Test
    void save() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        session.flush();
        session.clear();

        Pizza actualResult = session.get(Pizza.class, pizza.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        session.flush();
        session.clear();
        Pizza pizza2 = getPizza2(pizza.getId());
        pizzaDao.update(pizza2);
        session.flush();
        session.clear();

        Pizza actualResult = session.get(Pizza.class, pizza2.getId());

        assertThat(actualResult.getName()).isEqualTo(pizza2.getName());
    }

    @Test
    void delete() {
        Pizza pizza = getPizza();
        pizzaDao.save(pizza);
        session.flush();
        session.clear();
        pizzaDao.delete(pizza);
        session.clear();

        Pizza actualResult = session.get(Pizza.class, pizza.getId());

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