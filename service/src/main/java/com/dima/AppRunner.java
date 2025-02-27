package com.dima;

import com.dima.Enum.Role;
import com.dima.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class AppRunner {

    public static void main(String[] args) {

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
}
