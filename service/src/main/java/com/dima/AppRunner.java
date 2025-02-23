package com.dima;

import com.dima.Enum.Role;
import com.dima.entity.User;
import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class AppRunner {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 1);

            User user2 = User.builder()
                    .id(user.getId())
                    .firstname("Petr")
                    .lastname("Petrov")
                    .phoneNumber("4321")
                    .birthDate(LocalDate.of(2000, 12, 15))
                    .role(Role.USER)
                    .password("999")
                    .build();

            session.merge(user2);

            session.getTransaction().commit();
        }
    }
}
