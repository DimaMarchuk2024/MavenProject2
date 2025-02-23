package com.dima.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

class OrderPizzaTest {

    @Test
    void saveOrderPizza() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            OrderPizza orderPizza = OrderPizza.builder()
                    .pizzaToOrderId(1)
                    .ordersId(1)
                    .build();

            session.persist(orderPizza);

            session.getTransaction().commit();
        }
    }

    @Test
    void getOrderPizza() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            OrderPizza orderPizza = session.get(OrderPizza.class, 1);
            System.out.println(orderPizza);

            session.getTransaction().commit();
        }
    }
}