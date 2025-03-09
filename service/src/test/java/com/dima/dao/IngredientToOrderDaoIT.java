package com.dima.dao;

import com.dima.Enum.Role;
import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import com.dima.dao.impl.IngredientToOrderDao;
import com.dima.entity.Ingredient;
import com.dima.entity.IngredientToOrder;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import com.dima.util.HibernateUtil;
import com.dima.util.TestDataBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class IngredientToOrderDaoIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private Session session;
    private IngredientToOrderDao ingredientToOrderDao;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        ingredientToOrderDao = new IngredientToOrderDao(session);
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
        List<IngredientToOrder> actualResult = ingredientToOrderDao.findAll();

        assertThat(actualResult).hasSize(16);
    }

    @Test
    void findById() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        ingredientToOrderDao.save(ingredientToOrder);
        session.flush();
        session.clear();

        Optional<IngredientToOrder> actualResult = ingredientToOrderDao.getById(ingredientToOrder.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(ingredientToOrder.getId());
    }

    @Test
    void save() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        ingredientToOrderDao.save(ingredientToOrder);
        session.flush();
        session.clear();

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        ingredientToOrderDao.save(ingredientToOrder);
        session.flush();
        session.clear();
        IngredientToOrder ingredientToOrder2 = getIngredientToOrder2(ingredientToOrder.getId());
        ingredientToOrderDao.update(ingredientToOrder2);
        session.flush();
        session.clear();

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertThat(actualResult.getPizzaToOrder()).isEqualTo(ingredientToOrder2.getPizzaToOrder());
        assertThat(actualResult.getIngredient()).isEqualTo(ingredientToOrder2.getIngredient());
    }

    @Test
    void delete() {
        IngredientToOrder ingredientToOrder = getIngredientToOrder();
        ingredientToOrderDao.save(ingredientToOrder);
        session.flush();
        session.clear();
        ingredientToOrderDao.delete(ingredientToOrder);
        session.clear();

        IngredientToOrder actualResult = session.get(IngredientToOrder.class, ingredientToOrder.getId());

        assertNull(actualResult);
    }

    private IngredientToOrder getIngredientToOrder() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        Ingredient ingredient = getIngredient();

        return IngredientToOrder.builder()
                .pizzaToOrder(pizzaToOrder)
                .ingredient(ingredient)
                .build();
    }

    private PizzaToOrder getPizzaToOrder() {
        Pizza pizza = getPizza();
        User user = getUser();

        PizzaToOrder pizzaToOrder = PizzaToOrder.builder()
                .pizza(pizza)
                .size(Size.BIG)
                .type(TypeDough.TRADITIONAL)
                .count(1)
                .price(BigDecimal.valueOf(40).setScale(2))
                .user(user)
                .build();
        session.persist(pizzaToOrder);

        return pizzaToOrder;
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

    private Pizza getPizza() {
        Pizza pizza = Pizza.builder()
                .name("Vegan")
                .build();
        session.persist(pizza);

        return pizza;
    }

    private Ingredient getIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .name("Tomatoes")
                .price(BigDecimal.valueOf(2.5).setScale(2))
                .build();
        session.persist(ingredient);

        return ingredient;
    }

    private IngredientToOrder getIngredientToOrder2(Long id) {
        PizzaToOrder pizzaToOrder2 = getPizzaToOrder2();
        Ingredient ingredient2 = getIngredient2();

        return IngredientToOrder.builder()
                .id(id)
                .pizzaToOrder(pizzaToOrder2)
                .ingredient(ingredient2)
                .build();
    }

    private PizzaToOrder getPizzaToOrder2() {
        Pizza pizza2 = getPizza2();
        User user2 = getUser2();

        PizzaToOrder pizzaToOrder2 = PizzaToOrder.builder()
                .pizza(pizza2)
                .size(Size.BIG)
                .type(TypeDough.TRADITIONAL)
                .count(1)
                .price(BigDecimal.valueOf(40).setScale(2))
                .user(user2)
                .build();
        session.persist(pizzaToOrder2);

        return pizzaToOrder2;
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

    private Pizza getPizza2() {
        Pizza pizza = Pizza.builder()
                .name("Mexican")
                .build();
        session.persist(pizza);

        return pizza;
    }

    private Ingredient getIngredient2() {
        Ingredient ingredient2 = Ingredient.builder()
                .name("Pineapples")
                .price(BigDecimal.valueOf(3.0).setScale(2))
                .build();
        session.persist(ingredient2);

        return ingredient2;
    }
}