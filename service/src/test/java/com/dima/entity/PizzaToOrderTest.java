package com.dima.entity;

import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

class PizzaToOrderTest {

    @Test
    void savePizzaToOrder() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            PizzaToOrder pizzaToOrder = PizzaToOrder.builder()
                    .pizzaId(1)
                    .size(Size.MEDIUM)
                    .type(TypeDough.TRADITIONAL)
                    .numberPizza(1)
                    .price(35.99)
                    .userId(1L)
                    .build();

            session.persist(pizzaToOrder);

            session.getTransaction().commit();
        }
    }

    @Test
    void getPizzaToOrder() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            PizzaToOrder pizzaToOrder = session.get(PizzaToOrder.class, 1);
            System.out.println(pizzaToOrder);

            session.getTransaction().commit();
        }
    }
}