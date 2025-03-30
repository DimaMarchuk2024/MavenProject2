package com.dima.dao;

import com.dima.Enum.Role;
import com.dima.config.ApplicationConfiguration;
import com.dima.dao.impl.DeliveryAddressDao;
import com.dima.entity.DeliveryAddress;
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

class DeliveryAddressDaoIT {

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

    private final SessionFactory sessionFactory = context.getBean(SessionFactory.class);

    private final DeliveryAddressDao deliveryAddressDao = context.getBean(DeliveryAddressDao.class);

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
        List<DeliveryAddress> actualResult = deliveryAddressDao.findAll();

        assertThat(actualResult).hasSize(4);
        List<String> addresses = actualResult.stream().map(DeliveryAddress::getAddress).toList();
        assertThat(addresses).containsExactlyInAnyOrder("Moscow", "Kaluga", "Brest", "Minsk");
    }

    @Test
    void findById() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        deliveryAddressDao.save(deliveryAddress);
        session.flush();
        session.clear();

        Optional<DeliveryAddress> actualResult = deliveryAddressDao.getById(deliveryAddress.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(deliveryAddress.getId());
    }

    @Test
    void save() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        deliveryAddressDao.save(deliveryAddress);
        session.flush();
        session.clear();

        DeliveryAddress actualResult = session.get(DeliveryAddress.class, deliveryAddress.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        deliveryAddressDao.save(deliveryAddress);
        session.flush();
        session.clear();
        DeliveryAddress deliveryAddress2 = getDeliveryAddress2(deliveryAddress.getId());
        deliveryAddressDao.update(deliveryAddress2);
        session.flush();
        session.clear();

        DeliveryAddress actualResult = session.find(DeliveryAddress.class, deliveryAddress.getId());

        assertThat(actualResult.getUser()).isEqualTo(deliveryAddress2.getUser());
        assertThat(actualResult.getAddress()).isEqualTo(deliveryAddress2.getAddress());
    }

    @Test
    void delete() {
        DeliveryAddress deliveryAddress = getDeliveryAddress();
        deliveryAddressDao.save(deliveryAddress);
        session.flush();
        session.clear();
        deliveryAddressDao.delete(deliveryAddress);
        session.clear();

        DeliveryAddress actualResult = session.get(DeliveryAddress.class, deliveryAddress.getId());

        assertNull(actualResult);
    }

    private DeliveryAddress getDeliveryAddress() {
        User user = getUser();

        return DeliveryAddress.builder()
                .user(user)
                .address("Gomel")
                .build();
    }

    private User getUser() {
        User user = User.builder()
                .firstname("Max")
                .lastname("Maximov")
                .phoneNumber("99-99-999")
                .email("max@gmail.com")
                .birthDate(LocalDate.of(1995, 5, 20))
                .role(Role.ADMIN)
                .password("999")
                .build();
        session.persist(user);

        return user;
    }

    private DeliveryAddress getDeliveryAddress2(Long id) {
        User user2 = getUser2();

        return DeliveryAddress.builder()
                .id(id)
                .user(user2)
                .address("Grodno")
                .build();
    }

    private User getUser2() {
        User user = User.builder()
                .firstname("Leo")
                .lastname("Leonov")
                .phoneNumber("88-88-888")
                .email("leoax@gmail.com")
                .birthDate(LocalDate.of(1985, 10, 2))
                .role(Role.USER)
                .password("888")
                .build();
        session.persist(user);

        return user;
    }
}