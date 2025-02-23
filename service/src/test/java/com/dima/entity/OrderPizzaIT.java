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

class OrderPizzaIT {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void save() {
        OrderPizza orderPizza = getOrderPizza();

        session.persist(orderPizza);
        OrderPizza actualResult = session.get(OrderPizza.class, orderPizza.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        OrderPizza orderPizza = getOrderPizza();
        session.persist(orderPizza);

        OrderPizza actualResult = session.get(OrderPizza.class, orderPizza.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(orderPizza.getPizzaToOrder());
        assertThat(actualResult.getOrder()).isEqualTo(orderPizza.getOrder());
        assertThat(actualResult.getPrice()).isEqualTo(orderPizza.getPrice());
    }

    @Test
    void update() {
        OrderPizza orderPizza = getOrderPizza();
        session.persist(orderPizza);
        Pizza pizza2 = Pizza.builder()
                .name("Pepperoni2")
                .build();
        session.persist(pizza2);
        User user2 = User.builder()
                .firstname("Petr")
                .lastname("Petrov")
                .phoneNumber("4321")
                .birthDate(LocalDate.of(2000, 12, 15))
                .role(Role.USER)
                .password("999")
                .build();
        session.persist(user2);
        PizzaToOrder pizzaToOrder2 = PizzaToOrder.builder()
                .pizza(pizza2)
                .size(Size.SMALL)
                .type(TypeDough.THIN)
                .count(2)
                .price(BigDecimal.valueOf(70))
                .user(user2)
                .build();
        session.persist(pizzaToOrder2);
        Order order2 = Order.builder()
                .date(Instant.now().plusSeconds(500))
                .finalPrice(BigDecimal.valueOf(150))
                .build();
        session.persist(order2);
        OrderPizza orderPizza2 = OrderPizza.builder()
                .id(orderPizza.getId())
                .pizzaToOrder(pizzaToOrder2)
                .order(order2)
                .price(BigDecimal.valueOf(90))
                .build();

        session.merge(orderPizza2);
        OrderPizza actualResult = session.get(OrderPizza.class, orderPizza.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(orderPizza2.getPizzaToOrder());
        assertThat(actualResult.getOrder()).isEqualTo(orderPizza2.getOrder());
        assertThat(actualResult.getPrice()).isEqualTo(orderPizza2.getPrice());
    }

    @Test
    void delete() {
        OrderPizza orderPizza = getOrderPizza();
        session.persist(orderPizza);

        session.remove(orderPizza);
        OrderPizza actualResult = session.get(OrderPizza.class, orderPizza.getId());

        assertNull(actualResult);
    }

    private OrderPizza getOrderPizza() {
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
                .date(Instant.now())
                .finalPrice(BigDecimal.valueOf(100))
                .build();
        session.persist(order);

        return OrderPizza.builder()
                .pizzaToOrder(pizzaToOrder)
                .order(order)
                .price(BigDecimal.valueOf(70))
                .build();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }
}