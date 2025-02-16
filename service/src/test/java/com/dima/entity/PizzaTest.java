package com.dima.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

class PizzaTest {

    @Test
    void savePizza() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Pizza pizza = Pizza.builder()
                    .name("Pepperoni")
                    .build();

            session.persist(pizza);

            session.getTransaction().commit();
        }
    }

    @Test
    void getPizza() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Pizza pizza = session.get(Pizza.class, 1);
            System.out.println(pizza);

            session.getTransaction().commit();
        }
    }
}