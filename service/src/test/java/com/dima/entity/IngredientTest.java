package com.dima.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

class IngredientTest {

    @Test
    void saveIngredient() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Ingredient ingredient = Ingredient.builder()
                    .name("Mozzarella")
                    .price(2.7)
                    .build();

            session.persist(ingredient);

            session.getTransaction().commit();
        }
    }

    @Test
    void getIngredient() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Ingredient ingredient = session.get(Ingredient.class, 1);
            System.out.println(ingredient);

            session.getTransaction().commit();
        }
    }
}