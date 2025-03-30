package com.dima.integration.dao;

import com.dima.Enum.Role;
import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import com.dima.filter.PizzaFilter;
import com.dima.integration.annotation.IT;
import com.dima.util.TestDataBuilder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@IT
@RequiredArgsConstructor
class PizzaToOrderDaoIT {

    private final PizzaToOrderDao pizzaToOrderDao;

    private final EntityManager entityManager;

    @BeforeEach
    void init() {
        TestDataBuilder.builderData(entityManager);
    }

    @Test
    void findAllPizzaToOrderByFilter() {
        PizzaFilter pizzaFilter = PizzaFilter.builder()
                .pizzaName("Pepperoni")
                .build();

        List<PizzaToOrder> actualResult = pizzaToOrderDao.findAllPizzaToOrderByFilter(entityManager, pizzaFilter);

        assertThat(actualResult).hasSize(2);
        List<User> users = actualResult.stream().map(PizzaToOrder::getUser).toList();
        List<String> lastnames = users.stream().map(User::getLastname).toList();
        assertThat(lastnames).contains("Petrov", "Dimov");
    }

    @Test
    void findAll() {
        List<PizzaToOrder> actualResult = pizzaToOrderDao.findAll();

        assertThat(actualResult).hasSize(6);
        List<BigDecimal> prices = actualResult.stream().map(PizzaToOrder::getPrice).toList();
        assertThat(prices).containsExactlyInAnyOrder(
                BigDecimal.valueOf(25),
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(35),
                BigDecimal.valueOf(25),
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(35));
    }

    @Test
    void findById() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        pizzaToOrderDao.save(pizzaToOrder);
        entityManager.flush();
        entityManager.clear();

        Optional<PizzaToOrder> actualResult = pizzaToOrderDao.getById(pizzaToOrder.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(pizzaToOrder.getId());
    }

    @Test
    void save() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        pizzaToOrderDao.save(pizzaToOrder);
        entityManager.flush();
        entityManager.clear();

        PizzaToOrder actualResult = entityManager.find(PizzaToOrder.class, pizzaToOrder.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        pizzaToOrderDao.save(pizzaToOrder);
        entityManager.flush();
        entityManager.clear();
        PizzaToOrder pizzaToOrder2 = getPizzaToOrder2(pizzaToOrder.getId());
        pizzaToOrderDao.update(pizzaToOrder2);
        entityManager.flush();
        entityManager.clear();

        PizzaToOrder actualResult = entityManager.find(PizzaToOrder.class, pizzaToOrder.getId());

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
        pizzaToOrderDao.save(pizzaToOrder);
        entityManager.flush();
        pizzaToOrderDao.delete(pizzaToOrder);
        entityManager.clear();

        User actualResult = entityManager.find(User.class, pizzaToOrder.getId());

        assertNull(actualResult);
    }

    private PizzaToOrder getPizzaToOrder() {
        Pizza pizza = getPizza();
        User user = getUser();

        return PizzaToOrder.builder()
                .pizza(pizza)
                .size(Size.BIG)
                .type(TypeDough.TRADITIONAL)
                .count(1)
                .price(BigDecimal.valueOf(40).setScale(2))
                .user(user)
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
        entityManager.persist(user);

        return user;
    }

    private Pizza getPizza() {
        Pizza pizza = Pizza.builder()
                .name("Vegan")
                .build();
        entityManager.persist(pizza);

        return pizza;
    }

    private PizzaToOrder getPizzaToOrder2(Long id) {
        Pizza pizza2 = getPizza2();
        User user2 = getUser2();

        return PizzaToOrder.builder()
                .id(id)
                .pizza(pizza2)
                .size(Size.BIG)
                .type(TypeDough.TRADITIONAL)
                .count(1)
                .price(BigDecimal.valueOf(40).setScale(2))
                .user(user2)
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
        entityManager.persist(user);

        return user;
    }

    private Pizza getPizza2() {
        Pizza pizza = Pizza.builder()
                .name("Mexican")
                .build();
        entityManager.persist(pizza);

        return pizza;
    }
}