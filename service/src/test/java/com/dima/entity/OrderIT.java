package com.dima.entity;

import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderIT {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void save() {
        Order order = getOrder();

        session.persist(order);
        Order actualResult = session.get(Order.class, order.getId());

        assertNotNull(actualResult.getId());
    }


    @Test
    void get() {
        Order order = getOrder();
        session.persist(order);

        Order actualResult = session.get(Order.class, order.getId());

        assertThat(actualResult.getDate()).isEqualTo(order.getDate());
        assertThat(actualResult.getFinalPrice()).isEqualTo(order.getFinalPrice());
    }

    @Test
    void update() {
        Order order = getOrder();
        session.persist(order);
        Order order2 = Order.builder()
                .id(order.getId())
                .date(Instant.now().plusSeconds(500))
                .finalPrice(BigDecimal.valueOf(150))
                .build();

        session.merge(order2);
        Order actualResult = session.get(Order.class, order.getId());

        assertThat(actualResult.getDate()).isEqualTo(order2.getDate());
        assertThat(actualResult.getFinalPrice()).isEqualTo(order2.getFinalPrice());
    }

    @Test
    void delete() {
        Order order = getOrder();
        session.persist(order);

        session.remove(order);
        Order actualResult = session.get(Order.class, order.getId());

        assertNull(actualResult);
    }

    private static Order getOrder() {
        return Order.builder()
                .date(Instant.now())
                .finalPrice(BigDecimal.valueOf(100))
                .build();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }
}