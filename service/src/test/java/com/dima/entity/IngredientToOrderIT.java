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

public class IngredientToOrderIT {

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
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        session.persist(ingredientToOrder);
        session.flush();
        session.evict(ingredientToOrder);

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void get() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        session.persist(ingredientToOrder);
        session.flush();
        session.evict(ingredientToOrder);

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(ingredientToOrder.getPizzaToOrder());
        assertThat(actualResult.getIngredient()).isEqualTo(ingredientToOrder.getIngredient());
    }

    @Test
    void update() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        session.persist(ingredientToOrder);
        session.evict(ingredientToOrder);
        Pizza pizza2 = getPizza2();
        session.persist(pizza2);
        User user2 = getUser2();
        session.persist(user2);
        PizzaToOrder pizzaToOrder2 = getPizzaToOrder2(pizza2, user2);
        session.persist(pizzaToOrder2);
        Ingredient ingredient2 = getIngredient2();
        session.persist(ingredient2);
        IngredientToOrder ingredientToOrder2 = getIngredientToOrder2(ingredientToOrder, pizzaToOrder2, ingredient2);
        session.merge(ingredientToOrder2);
        session.flush();
        session.evict(ingredientToOrder2);

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(ingredientToOrder2.getPizzaToOrder());
        assertThat(actualResult.getIngredient()).isEqualTo(ingredientToOrder2.getIngredient());
    }

    @Test
    void delete() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        session.persist(ingredientToOrder);
        session.remove(ingredientToOrder);
        session.flush();
        session.evict(ingredientToOrder);

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertNull(actualResult);
    }

    private static IngredientToOrder getIngredientToOrder2(IngredientToOrder ingredientToOrder, PizzaToOrder pizzaToOrder2, Ingredient ingredient2) {
        return IngredientToOrder.builder()
                .id(ingredientToOrder.getId())
                .pizzaToOrder(pizzaToOrder2)
                .ingredient(ingredient2)
                .build();
    }

    private static Ingredient getIngredient2() {
        return Ingredient.builder()
                .name("Mozzarella2")
                .price(BigDecimal.valueOf(3.9))
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

    private IngredientToOrder getIngredientToOrder() {
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

        Ingredient ingredient = Ingredient.builder()
                .name("Mozzarella")
                .price(BigDecimal.valueOf(3.5))
                .build();
        session.persist(ingredient);

        return IngredientToOrder.builder()
                .pizzaToOrder(pizzaToOrder)
                .ingredient(ingredient)
                .build();
    }
}
