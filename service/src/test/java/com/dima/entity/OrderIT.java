package com.dima.entity;

import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderIT {

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
        Order order = getOrder();
        session.persist(order);
        session.flush();
        session.evict(order);

        Order actualResult = session.get(Order.class, order.getId());

        assertNotNull(actualResult.getId());
    }


    @Test
    void get() {
        Order order = getOrder();
        session.persist(order);
        session.flush();
        session.evict(order);

        Order actualResult = session.get(Order.class, order.getId());

        assertThat(actualResult.getDateTime()).isEqualTo(order.getDateTime());
        assertThat(actualResult.getFinalPrice()).isEqualTo(order.getFinalPrice());
    }

    @Test
    void update() {
        Order order = getOrder();
        session.persist(order);
        session.evict(order);
        Order order2 = getOrder2(order);
        session.merge(order2);
        session.flush();
        session.evict(order2);

        Order actualResult = session.get(Order.class, order.getId());

        assertThat(actualResult.getDateTime()).isEqualTo(order2.getDateTime());
        assertThat(actualResult.getFinalPrice()).isEqualTo(order2.getFinalPrice());
    }

    @Test
    void delete() {
        Order order = getOrder();
        session.persist(order);
        session.remove(order);
        session.flush();
        session.evict(order);

        Order actualResult = session.get(Order.class, order.getId());

        assertNull(actualResult);
    }

    private static Order getOrder2(Order order) {
        return Order.builder()
                .id(order.getId())
                .dateTime(Instant.now().plusSeconds(500).truncatedTo(ChronoUnit.SECONDS))
                .finalPrice(BigDecimal.valueOf(150).setScale(2))
                .build();
    }

    private static Order getOrder() {
        return Order.builder()
                .dateTime(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .finalPrice(BigDecimal.valueOf(100).setScale(2))
                .build();
    }
}