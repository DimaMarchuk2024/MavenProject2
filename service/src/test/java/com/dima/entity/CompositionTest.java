package com.dima.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

class CompositionTest {

    @Test
    void saveComposition() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Composition composition = Composition.builder()
                    .pizzaId(1)
                    .ingredientId(1)
                    .build();
            session.persist(composition);

            session.getTransaction().commit();
        }
    }

    @Test
    void getComposition() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Composition composition = session.get(Composition.class, 2);
            System.out.println(composition);

            session.getTransaction().commit();
        }
    }
}