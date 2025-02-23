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

class DeliveryAddressIT {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void save() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();

        session.persist(deliveryAddress);
        DeliveryAddress actualResult = session.get(DeliveryAddress.class, deliveryAddress.getId());

        assertNotNull(actualResult.getId());
    }


    @Test
    void get() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        session.persist(deliveryAddress);
        DeliveryAddress actualResult = session.get(DeliveryAddress.class, deliveryAddress.getId());

        assertThat(actualResult.getAddress()).isEqualTo(deliveryAddress.getAddress());
        assertThat(actualResult.getUser()).isEqualTo(deliveryAddress.getUser());
    }

    @Test
    void update() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        session.persist(deliveryAddress);
        User user2 = User.builder()
                .firstname("Petr")
                .lastname("Petrov")
                .phoneNumber("4321")
                .birthDate(LocalDate.of(2000, 12, 1))
                .role(Role.USER)
                .password("999")
                .build();
        session.persist(user2);
        DeliveryAddress deliveryAddress2 = DeliveryAddress.builder()
                .id(deliveryAddress.getId())
                .user(user2)
                .address("Sovetskaja, 20")
                .build();

        session.merge(deliveryAddress2);
        DeliveryAddress actualResult = session.get(DeliveryAddress.class, deliveryAddress.getId());

        assertThat(actualResult.getUser()).isEqualTo(deliveryAddress2.getUser());
        assertThat(actualResult.getAddress()).isEqualTo(deliveryAddress2.getAddress());
    }

    @Test
    void delete() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        session.persist(deliveryAddress);

        session.remove(deliveryAddress);
        DeliveryAddress actualResult = session.get(DeliveryAddress.class, deliveryAddress.getId());

        assertNull(actualResult);
    }

    private DeliveryAddress getDeliveryAddress() {
        User user = User.builder()
                .firstname("Ivan")
                .lastname("Ivanov")
                .phoneNumber("1234")
                .birthDate(LocalDate.of(1999, 1, 30))
                .role(Role.ADMIN)
                .password("111")
                .build();
        session.persist(user);

        return DeliveryAddress.builder()
                .user(user)
                .address("Lenina, 15")
                .build();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }
}