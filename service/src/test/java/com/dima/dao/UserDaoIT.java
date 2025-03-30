package com.dima.dao;

import com.dima.Enum.Role;
import com.dima.config.ApplicationConfiguration;
import com.dima.dao.impl.UserDao;
import com.dima.entity.User;
import com.dima.util.TestDataBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDaoIT {
    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

    private final SessionFactory sessionFactory = context.getBean(SessionFactory.class);

    private final UserDao userDao = context.getBean(UserDao.class);

    private Session session = (Session) context.getBean(EntityManager.class);

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataBuilder.builderData(session);
    }

    @AfterEach
    void rollback() {
        session.getTransaction().rollback();
        session.close();
        context.close();
    }

    @Test
    void findAll() {
        List<User> actualResult = userDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<String> firstNames = actualResult.stream().map(User::getFirstname).toList();
        assertThat(firstNames).containsExactlyInAnyOrder("Ivan","Petr", "Dima");
    }

    @Test
    void findById() {
        User user = getUser();
        userDao.save(user);
        session.flush();
        session.evict(user);

        Optional<User> actualResult = userDao.getById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void save() {
        User user = getUser();
        userDao.save(user);
        session.flush();
        session.evict(user);

        User actualResult = session.get(User.class, user.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        User user = getUser();
        userDao.save(user);
        session.flush();
        session.clear();
        User user2 = getUser2(user.getId());
        userDao.update(user2);
        session.flush();
        session.clear();

        User actualResult = session.get(User.class, user.getId());

        assertThat(actualResult.getFirstname()).isEqualTo(user2.getFirstname());
        assertThat(actualResult.getLastname()).isEqualTo(user2.getLastname());
        assertThat(actualResult.getBirthDate()).isEqualTo(user2.getBirthDate());
        assertThat(actualResult.getEmail()).isEqualTo(user2.getEmail());
        assertThat(actualResult.getPhoneNumber()).isEqualTo(user2.getPhoneNumber());
        assertThat(actualResult.getRole()).isEqualTo(user2.getRole());
        assertThat(actualResult.getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    void delete() {
        User user = getUser();
        userDao.save(user);
        session.flush();
        session.clear();
        userDao.delete(user);
        session.clear();

        User actualResult = session.get(User.class, user.getId());

        assertNull(actualResult);
    }

    private User getUser() {
        return User.builder()
                .firstname("Max")
                .lastname("Maximov")
                .phoneNumber("99-99-999")
                .email("max@gmail.com")
                .birthDate(LocalDate.of(1992, 5, 25))
                .role(Role.ADMIN)
                .password("999")
                .build();
    }

    private User getUser2(Long id) {
        return User.builder()
                .id(id)
                .firstname("Leo")
                .lastname("Leonov")
                .phoneNumber("88-88-888")
                .email("leoax@gmail.com")
                .birthDate(LocalDate.of(1985, 10, 2))
                .role(Role.USER)
                .password("888")
                .build();
    }
}