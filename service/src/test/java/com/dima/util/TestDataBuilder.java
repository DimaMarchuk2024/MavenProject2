package com.dima.util;

import com.dima.Enum.Role;
import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import com.dima.entity.DeliveryAddress;
import com.dima.entity.Ingredient;
import com.dima.entity.IngredientToOrder;
import com.dima.entity.Order;
import com.dima.entity.OrderDetail;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaIngredient;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class TestDataBuilder {

    public void builderData(EntityManager session) {

        User ivan = saveUser(session, "Ivan", "Ivanov",
                "11-11-111", "ivan@gmail.com", Role.ADMIN,
                LocalDate.of(1995, 5, 15), "111");
        User petr = saveUser(session, "Petr", "Petrov",
                "22-22-222", "petr@gmail.com", Role.USER,
                LocalDate.of(1999, 7, 4), "222");
        User dima = saveUser(session, "Dima", "Dimov",
                "33-33-333", "dima@gmail.com", Role.USER,
                LocalDate.of(1990, 11, 28), "333");

        saveDeliveryAddress(session, ivan, "Moscow");
        saveDeliveryAddress(session, ivan, "Kaluga");
        saveDeliveryAddress(session, petr, "Brest");
        saveDeliveryAddress(session, dima, "Minsk");

        Pizza pepperoni = savePizza(session, "Pepperoni");
        Pizza italian = savePizza(session, "Italian");
        Pizza fourCheeses = savePizza(session, "Four cheeses");

        Ingredient mozzarella = saveIngredient(session, "Mozzarella", BigDecimal.valueOf(3.5));
        Ingredient tomatoSauce = saveIngredient(session, "Tomato sauce", BigDecimal.valueOf(2.5));
        Ingredient bacon = saveIngredient(session, "Bacon", BigDecimal.valueOf(3));
        Ingredient parmesan = saveIngredient(session, "Parmesan", BigDecimal.valueOf(3.5));
        Ingredient onion = saveIngredient(session, "Onion", BigDecimal.valueOf(2));

        savePizzaIngredient(session, pepperoni, mozzarella);
        savePizzaIngredient(session, pepperoni, tomatoSauce);
        savePizzaIngredient(session, italian, mozzarella);
        savePizzaIngredient(session, italian, bacon);
        savePizzaIngredient(session, italian, onion);
        savePizzaIngredient(session, fourCheeses, mozzarella);
        savePizzaIngredient(session, fourCheeses, parmesan);
        savePizzaIngredient(session, fourCheeses, onion);

        PizzaToOrder pizzaToOrderPetr = savePizzaToOrder(session, pepperoni, Size.BIG, TypeDough.THIN,
                1, BigDecimal.valueOf(35), petr);
        PizzaToOrder pizzaToOrderPetr2 = savePizzaToOrder(session, italian, Size.SMALL, TypeDough.THIN,
                2, BigDecimal.valueOf(25), petr);
        PizzaToOrder pizzaToOrderDima = savePizzaToOrder(session, pepperoni, Size.MEDIUM, TypeDough.TRADITIONAL,
                2, BigDecimal.valueOf(30), dima);
        PizzaToOrder pizzaToOrderDima2 = savePizzaToOrder(session, fourCheeses, Size.BIG, TypeDough.THIN,
                2, BigDecimal.valueOf(35), dima);
        PizzaToOrder pizzaToOrderDima3 = savePizzaToOrder(session, italian, Size.SMALL, TypeDough.TRADITIONAL,
                1, BigDecimal.valueOf(25), dima);
        PizzaToOrder pizzaToOrderIvan = savePizzaToOrder(session, fourCheeses, Size.MEDIUM, TypeDough.TRADITIONAL,
                3, BigDecimal.valueOf(30), ivan);

        saveIngredientToOrder(session, pizzaToOrderPetr, mozzarella);
        saveIngredientToOrder(session, pizzaToOrderPetr, tomatoSauce);
        saveIngredientToOrder(session, pizzaToOrderPetr2, mozzarella);
        saveIngredientToOrder(session, pizzaToOrderPetr2, bacon);
        saveIngredientToOrder(session, pizzaToOrderPetr2, onion);
        saveIngredientToOrder(session, pizzaToOrderDima, mozzarella);
        saveIngredientToOrder(session, pizzaToOrderDima, tomatoSauce);
        saveIngredientToOrder(session, pizzaToOrderDima2, mozzarella);
        saveIngredientToOrder(session, pizzaToOrderDima2, parmesan);
        saveIngredientToOrder(session, pizzaToOrderDima2, onion);
        saveIngredientToOrder(session, pizzaToOrderDima3, mozzarella);
        saveIngredientToOrder(session, pizzaToOrderDima3, bacon);
        saveIngredientToOrder(session, pizzaToOrderDima3, onion);
        saveIngredientToOrder(session, pizzaToOrderIvan, mozzarella);
        saveIngredientToOrder(session, pizzaToOrderIvan, parmesan);
        saveIngredientToOrder(session, pizzaToOrderIvan, onion);

        Order order1 = saveOrder(session, Instant.now().minus(20, ChronoUnit.DAYS), BigDecimal.valueOf(85));
        Order order2 = saveOrder(session, Instant.now().minus(15, ChronoUnit.DAYS), BigDecimal.valueOf(60));
        Order order3 = saveOrder(session, Instant.now().minus(13, ChronoUnit.DAYS), BigDecimal.valueOf(90));
        Order order4 = saveOrder(session, Instant.now().minus(10, ChronoUnit.DAYS), BigDecimal.valueOf(95));

        saveOrderDetail(session, pizzaToOrderPetr, order1, BigDecimal.valueOf(35));
        saveOrderDetail(session, pizzaToOrderPetr2, order1, BigDecimal.valueOf(50));
        saveOrderDetail(session, pizzaToOrderDima, order2, BigDecimal.valueOf(60));
        saveOrderDetail(session, pizzaToOrderIvan, order3, BigDecimal.valueOf(90));
        saveOrderDetail(session, pizzaToOrderDima2, order4, BigDecimal.valueOf(70));
        saveOrderDetail(session, pizzaToOrderDima3, order4, BigDecimal.valueOf(25));
    }

    private User saveUser(EntityManager session,
                          String firstname,
                          String lastname,
                          String phoneNumber,
                          String email,
                          Role role,
                          LocalDate birthDate,
                          String password) {
        User user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .phoneNumber(phoneNumber)
                .email(email)
                .role(role)
                .birthDate(birthDate)
                .password(password)
                .build();
        session.persist(user);

        return user;
    }

    private DeliveryAddress saveDeliveryAddress(EntityManager session,
                                                User user,
                                                String address) {
        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .user(user)
                .address(address)
                .build();
        session.persist(deliveryAddress);

        return deliveryAddress;
    }

    private Pizza savePizza(EntityManager session,
                            String name) {
        Pizza pizza = Pizza.builder()
                .name(name)
                .build();
        session.persist(pizza);

        return pizza;
    }

    private Ingredient saveIngredient(EntityManager session,
                                      String name,
                                      BigDecimal price) {
        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .price(price)
                .build();
        session.persist(ingredient);

        return ingredient;
    }

    private PizzaIngredient savePizzaIngredient(EntityManager session,
                                                Pizza pizza,
                                                Ingredient ingredient) {
        PizzaIngredient pizzaIngredient = PizzaIngredient.builder()
                .pizza(pizza)
                .ingredient(ingredient)
                .build();
        session.persist(pizzaIngredient);

        return pizzaIngredient;
    }

    private PizzaToOrder savePizzaToOrder(EntityManager session,
                                          Pizza pizza,
                                          Size size,
                                          TypeDough type,
                                          Integer count,
                                          BigDecimal price,
                                          User user) {
        PizzaToOrder pizzaToOrder = PizzaToOrder.builder()
                .pizza(pizza)
                .size(size)
                .type(type)
                .count(count)
                .price(price)
                .user(user)
                .build();
        session.persist(pizzaToOrder);

        return pizzaToOrder;
    }

    private IngredientToOrder saveIngredientToOrder(EntityManager session,
                                                    PizzaToOrder pizzaToOrder,
                                                    Ingredient ingredient) {
        IngredientToOrder ingredientToOrder = IngredientToOrder.builder()
                .pizzaToOrder(pizzaToOrder)
                .ingredient(ingredient)
                .build();
        session.persist(ingredientToOrder);

        return ingredientToOrder;
    }

    private Order saveOrder(EntityManager session,
                            Instant date,
                            BigDecimal finalPrice) {
        Order order = Order.builder()
                .dateTime(date)
                .finalPrice(finalPrice)
                .build();
        session.persist(order);

        return order;
    }

    private OrderDetail saveOrderDetail(EntityManager session,
                                        PizzaToOrder pizzaToOrder,
                                        Order order,
                                        BigDecimal price) {
        OrderDetail orderDetail = OrderDetail.builder()
                .pizzaToOrder(pizzaToOrder)
                .order(order)
                .price(price)
                .build();
        session.persist(orderDetail);

        return orderDetail;
    }
}
