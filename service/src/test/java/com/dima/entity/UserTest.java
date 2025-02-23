package com.dima.entity;

import com.dima.Enum.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserTest {

    @Test
    void saveUser() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .phoneNumber("123")
                    .birthDate(LocalDate.of(2020, 5, 12))
                    .role(Role.ADMIN)
                    .password("123")
                    .build();
            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void getUser() {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 1);
            System.out.println(user);

            session.getTransaction().commit();
        }
    }
}