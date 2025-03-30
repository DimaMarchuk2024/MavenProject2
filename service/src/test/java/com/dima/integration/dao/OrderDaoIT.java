package com.dima.integration.dao;

import com.dima.dao.impl.OrderDao;
import com.dima.entity.Order;
import com.dima.integration.annotation.IT;
import com.dima.util.TestDataBuilder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@IT
@RequiredArgsConstructor
class OrderDaoIT {

    private final OrderDao orderDao;

    private final EntityManager entityManager;

    @BeforeEach
    void init() {
        TestDataBuilder.builderData(entityManager);
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
        entityManager.flush();
        entityManager.clear();

        Optional<Order> actualResult = orderDao.getById(order.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(order.getId());
    }

    @Test
    void save() {
        Order order = getOrder();
        orderDao.save(order);
        entityManager.flush();
        entityManager.clear();

        Order actualResult = entityManager.find(Order.class, order.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Order order = getOrder();
        orderDao.save(order);
        entityManager.flush();
        entityManager.clear();
        Order order2 = getOrder2(order.getId());
        orderDao.update(order2);
        entityManager.flush();
        entityManager.clear();

        Order actualResult = entityManager.find(Order.class, order.getId());

        assertThat(actualResult.getDateTime()).isEqualTo(order2.getDateTime());
        assertThat(actualResult.getFinalPrice()).isEqualTo(order2.getFinalPrice());
    }

    @Test
    void delete() {
        Order order = getOrder();
        orderDao.save(order);
        entityManager.flush();
        orderDao.delete(order);
        entityManager.clear();

        Order actualResult = entityManager.find(Order.class, order.getId());

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