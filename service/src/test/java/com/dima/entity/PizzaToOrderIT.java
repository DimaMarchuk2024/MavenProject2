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
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PizzaToOrderIT {

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
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        session.persist(pizzaToOrder);
        session.flush();
        session.evict(pizzaToOrder);

        PizzaToOrder actualResult = session.get(PizzaToOrder.class, pizzaToOrder.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        session.persist(pizzaToOrder);
        session.flush();
        session.evict(pizzaToOrder);

        PizzaToOrder actualResult = session.get(PizzaToOrder.class, pizzaToOrder.getId());

        assertThat(actualResult).isEqualTo(pizzaToOrder);
    }

    @Test
    void update() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        session.persist(pizzaToOrder);
        session.evict(pizzaToOrder);
        Pizza pizza2 = getPizza2();
        session.persist(pizza2);
        User user2 = getUser2();
        session.persist(user2);
        PizzaToOrder pizzaToOrder2 = getPizzaToOrder2(pizzaToOrder, pizza2, user2);
        session.merge(pizzaToOrder2);
        session.flush();
        session.evict(pizzaToOrder2);

        PizzaToOrder actualResult = session.get(PizzaToOrder.class, pizzaToOrder.getId());

        assertThat(actualResult.getPizza()).isEqualTo(pizzaToOrder2.getPizza());
        assertThat(actualResult.getSize()).isEqualTo(pizzaToOrder2.getSize());
        assertThat(actualResult.getType()).isEqualTo(pizzaToOrder2.getType());
        assertThat(actualResult.getCount()).isEqualTo(pizzaToOrder2.getCount());
        assertThat(actualResult.getPrice()).isEqualTo(pizzaToOrder2.getPrice());
        assertThat(actualResult.getUser()).isEqualTo(pizzaToOrder2.getUser());
    }

    @Test
    void delete() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        session.persist(pizzaToOrder);
        session.remove(pizzaToOrder);
        session.flush();
        session.evict(pizzaToOrder);

        PizzaToOrder actualResult = session.get(PizzaToOrder.class, pizzaToOrder.getId());

        assertNull(actualResult);
    }

    private static PizzaToOrder getPizzaToOrder2(PizzaToOrder pizzaToOrder, Pizza pizza2, User user2) {
        return PizzaToOrder.builder()
                .id(pizzaToOrder.getId())
                .pizza(pizza2)
                .size(Size.MEDIUM)
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
                .birthDate(LocalDate.of(2000, 12, 1))
                .role(Role.USER)
                .password("999")
                .build();
    }

    private static Pizza getPizza2() {
        return Pizza.builder()
                .name("Pepperoni2")
                .build();
    }

    private PizzaToOrder getPizzaToOrder() {
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

        return PizzaToOrder.builder()
                .pizza(pizza)
                .size(Size.BIG)
                .type(TypeDough.TRADITIONAL)
                .count(1)
                .price(BigDecimal.valueOf(35).setScale(2))
                .user(user)
                .build();
    }
}