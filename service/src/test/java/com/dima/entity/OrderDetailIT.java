package com.dima.entity;

import com.dima.Enum.Role;
import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderDetailIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void save() {
        OrderDetail orderDetail = getOrderDetail();
        session.persist(orderDetail);
        session.flush();
        session.evict(orderDetail);

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        OrderDetail orderDetail = getOrderDetail();
        session.persist(orderDetail);
        session.flush();
        session.evict(orderDetail);

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(orderDetail.getPizzaToOrder());
        assertThat(actualResult.getOrder()).isEqualTo(orderDetail.getOrder());
        assertThat(actualResult.getPrice()).isEqualTo(orderDetail.getPrice());
    }

    @Test
    void update() {
        OrderDetail orderDetail = getOrderDetail();
        session.persist(orderDetail);
        session.evict(orderDetail);
        Pizza pizza2 = getPizza2();
        session.persist(pizza2);
        User user2 = getUser2();
        session.persist(user2);
        PizzaToOrder pizzaToOrder2 = getPizzaToOrder2(pizza2, user2);
        session.persist(pizzaToOrder2);
        Order order2 = getOrder2();
        session.persist(order2);
        OrderDetail orderDetail2 = getOrderDetail2(orderDetail, pizzaToOrder2, order2);
        session.merge(orderDetail2);
        session.flush();
        session.evict(orderDetail2);

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(orderDetail2.getPizzaToOrder());
        assertThat(actualResult.getOrder()).isEqualTo(orderDetail2.getOrder());
        assertThat(actualResult.getPrice()).isEqualTo(orderDetail2.getPrice());
    }

    @Test
    void delete() {
        OrderDetail orderDetail = getOrderDetail();
        session.persist(orderDetail);
        session.remove(orderDetail);
        session.flush();
        session.evict(orderDetail);

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertNull(actualResult);
    }

    private static OrderDetail getOrderDetail2(OrderDetail orderDetail, PizzaToOrder pizzaToOrder2, Order order2) {
        return OrderDetail.builder()
                .id(orderDetail.getId())
                .pizzaToOrder(pizzaToOrder2)
                .order(order2)
                .price(BigDecimal.valueOf(90))
                .build();
    }

    private static Order getOrder2() {
        return Order.builder()
                .dateTime(Instant.now().plusSeconds(500))
                .finalPrice(BigDecimal.valueOf(150))
                .build();
    }

    private static PizzaToOrder getPizzaToOrder2(Pizza pizza2, User user2) {
        return PizzaToOrder.builder()
                .pizza(pizza2)
                .size(Size.SMALL)
                .type(TypeDough.THIN)
                .count(2)
                .price(BigDecimal.valueOf(70))
                .user(user2)
                .build();
    }

    private static User getUser2() {
        return User.builder()
                .firstname("Petr")
                .lastname("Petrov")
                .phoneNumber("4321")
                .birthDate(LocalDate.of(2000, 12, 15))
                .role(Role.USER)
                .password("999")
                .build();
    }

    private static Pizza getPizza2() {
        return Pizza.builder()
                .name("Pepperoni2")
                .build();
    }

    private OrderDetail getOrderDetail() {
        Pizza pizza = Pizza.builder()
                .name("Pepperoni")
                .build();
        session.persist(pizza);

        User user = User.builder()
                .firstname("Ivan")
                .lastname("Ivanov")
                .phoneNumber("1234")
                .birthDate(LocalDate.of(1999, 1, 30))
                .role(Role.ADMIN)
                .password("111")
                .build();
        session.persist(user);

        PizzaToOrder pizzaToOrder = PizzaToOrder.builder()
                .pizza(pizza)
                .size(Size.BIG)
                .type(TypeDough.TRADITIONAL)
                .count(1)
                .price(BigDecimal.valueOf(35))
                .user(user)
                .build();
        session.persist(pizzaToOrder);

        Order order = Order.builder()
                .dateTime(Instant.now())
                .finalPrice(BigDecimal.valueOf(100))
                .build();
        session.persist(order);

        return OrderDetail.builder()
                .pizzaToOrder(pizzaToOrder)
                .order(order)
                .price(BigDecimal.valueOf(70).setScale(2))
                .build();
    }
}