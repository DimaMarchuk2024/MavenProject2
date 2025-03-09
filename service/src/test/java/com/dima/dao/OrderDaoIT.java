package com.dima.dao;

import com.dima.dao.impl.OrderDao;
import com.dima.entity.Order;
import com.dima.util.HibernateUtil;
import com.dima.util.TestDataBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderDaoIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private Session session;
    private OrderDao orderDao;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        orderDao = new OrderDao(session);
        session.beginTransaction();
        TestDataBuilder.builderData(session);
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void findAll() {
        List<Order> actualResult = orderDao.findAll();

        assertThat(actualResult).hasSize(4);
        List<BigDecimal> finalPrices = actualResult.stream().map(Order::getFinalPrice).toList();
        assertThat(finalPrices).containsExactlyInAnyOrder(
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(85),
                BigDecimal.valueOf(90),
                BigDecimal.valueOf(95));
    }

    @Test
    void findById() {
        Order order = getOrder();
        orderDao.save(order);
        session.flush();
        session.clear();

        Optional<Order> actualResult = orderDao.getById(order.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(order.getId());
    }

    @Test
    void save() {
        Order order = getOrder();
        orderDao.save(order);
        session.flush();
        session.clear();

        Order actualResult = session.get(Order.class, order.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Order order = getOrder();
        orderDao.save(order);
        session.flush();
        session.clear();
        Order order2 = getOrder2(order.getId());
        orderDao.update(order2);
        session.flush();
        session.clear();

        Order actualResult = session.get(Order.class, order.getId());

        assertThat(actualResult.getDateTime()).isEqualTo(order2.getDateTime());
        assertThat(actualResult.getFinalPrice()).isEqualTo(order2.getFinalPrice());
    }

    @Test
    void delete() {
        Order order = getOrder();
        orderDao.save(order);
        session.flush();
        session.clear();
        orderDao.delete(order);
        session.clear();

        Order actualResult = session.get(Order.class, order.getId());

        assertNull(actualResult);
    }

    private static Order getOrder() {
        return Order.builder()
                .dateTime(Instant.now().minus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .finalPrice(BigDecimal.valueOf(80).setScale(2))
                .build();
    }

    private static Order getOrder2(Long id) {
        return Order.builder()
                .id(id)
                .dateTime(Instant.now().minus(3, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .finalPrice(BigDecimal.valueOf(75).setScale(2))
                .build();
    }
}