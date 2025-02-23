package com.dima.entity;

import com.dima.Enum.Role;
import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserIT {

    Session session = null;
    SessionFactory  sessionFactory = HibernateUtil.buildSessionFactory();

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void save() {
        User user = getUser();

        session.persist(user);
        User actualResult = session.get(User.class, user.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        User user = getUser();

        session.persist(user);
        User actualResult = session.get(User.class, user.getId());

        assertThat(actualResult).isEqualTo(user);
    }

    @Test
    void update() {
        User user = getUser();
        session.persist(user);
        User user2 = User.builder()
                .id(user.getId())
                .firstname("Petr")
                .lastname("Petrov")
                .phoneNumber("4321")
                .birthDate(LocalDate.of(2000, 12, 1))
                .role(Role.USER)
                .password("999")
                .build();

        session.merge(user2);
        User actualResult = session.get(User.class, user.getId());

        assertThat(actualResult.getFirstname()).isEqualTo(user2.getFirstname());
        assertThat(actualResult.getLastname()).isEqualTo(user2.getLastname());
        assertThat(actualResult.getPhoneNumber()).isEqualTo(user2.getPhoneNumber());
        assertThat(actualResult.getBirthDate()).isEqualTo(user2.getBirthDate());
        assertThat(actualResult.getRole()).isEqualTo(user2.getRole());
        assertThat(actualResult.getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    void delete() {
        User user = getUser();
        session.persist(user);

        session.remove(user);
        User actualResult = session.get(User.class, user.getId());

        assertNull(actualResult);
    }

    private static User getUser() {
        return User.builder()
                .firstname("Ivan")
                .lastname("Ivanov")
                .phoneNumber("1234")
                .birthDate(LocalDate.of(1999, 1, 30))
                .role(Role.ADMIN)
                .password("111")
                .build();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }
}