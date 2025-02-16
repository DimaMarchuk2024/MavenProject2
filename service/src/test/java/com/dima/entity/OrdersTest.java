package com.dima.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class OrdersTest {

    @Test
    void saveOrders() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Orders orders = Orders.builder()
                    .dateOrder(LocalDateTime.now())
                    .finalPrice(35.99)
                    .build();

            session.persist(orders);

            session.getTransaction().commit();
        }
    }

    @Test
    void getOrders() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Orders orders = session.get(Orders.class, 1);
            System.out.println(orders);

            session.getTransaction().commit();
        }
    }
}