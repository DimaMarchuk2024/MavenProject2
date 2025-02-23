package com.dima.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

class DeliveryAddressTest {

    @Test
    void saveDeliveryAddress() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                    .usersId(1L)
                    .address("Moscow")
                    .build();

            session.persist(deliveryAddress);

            session.getTransaction().commit();
        }
    }

    @Test
    void getDeliveryAddress() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            DeliveryAddress deliveryAddress = session.get(DeliveryAddress.class, 1);
            System.out.println(deliveryAddress);

            session.getTransaction().commit();
        }
    }
}