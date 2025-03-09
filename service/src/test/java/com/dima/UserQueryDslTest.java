package com.dima;

import com.dima.entity.OrderDetail;
import com.dima.filter.PizzaFilter;
import com.dima.filter.UserFilter;
import com.dima.entity.Order;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import com.dima.util.HibernateUtil;
import com.dima.util.TestDataBuilder;
import com.querydsl.core.Tuple;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserQueryDslTest {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    UserQueryDsl userQueryDsl = UserQueryDsl.getInstance();
    private final Session session = sessionFactory.openSession();

    @BeforeEach
    void init() {
        TestDataBuilder.builderData(session);
        session.beginTransaction();
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void findAll() {
        List<User> actualResult = userQueryDsl.findAll(session);

        assertThat(actualResult).hasSize(3);

        List<String> firstNames = actualResult.stream().map(User::getFirstname).toList();
        assertThat(firstNames).containsExactlyInAnyOrder("Ivan","Petr", "Dima");
    }

    @Test
    void findAllByFirstName() {

        UserFilter userFilter = UserFilter.builder()
                .firstname("Ivan")
                .build();

        List<User> actualResult = userQueryDsl.findAllByFirstName(session, userFilter);

        assertThat(actualResult).hasSize(1);

        assertThat(actualResult.get(0).getFirstname()).isEqualTo("Ivan");
    }

    @Test
    void findLimitedUsersOrderedByBirthday() {
        int limit = 2;
        List<User> actualResult = userQueryDsl.findLimitedUsersOrderedByBirthday(session, limit);

        assertThat(actualResult).hasSize(limit);

        List<String> firstNames = actualResult.stream().map(User::getFirstname).toList();
        assertThat(firstNames).contains("Dima", "Ivan");
    }

    @Test
    void findAllPizzaToOrderByPizzaName() {
        PizzaFilter pizzaFilter = PizzaFilter.builder()
                .pizzaName("Pepperoni")
                .build();

        List<PizzaToOrder> actualResult = userQueryDsl.findAllPizzaToOrderByPizzaName(session, pizzaFilter);

        assertThat(actualResult).hasSize(2);

        List<User> users = actualResult.stream().map(PizzaToOrder::getUser).toList();
        List<String> lastnames = users.stream().map(User::getLastname).toList();
        assertThat(lastnames).contains("Petrov", "Dimov");
    }

    @Test
    void findAllOrdersByPizzaName() {
        PizzaFilter pizzaFilter = PizzaFilter.builder()
                .pizzaName("Italian")
                .build();

        List<OrderDetail> actualResult = userQueryDsl.findAllOrdersByPizzaName(session, pizzaFilter);

        assertThat(actualResult).hasSize(2);

        List<Order> orders = actualResult.stream().map(OrderDetail::getOrder).toList();
        List<BigDecimal> finalPrices = orders.stream().map(Order::getFinalPrice).toList();
        assertThat(finalPrices).contains(BigDecimal.valueOf(85),BigDecimal.valueOf(95));
    }

    @Test
    void findAverageCountPizzaToOrderByFirstAndLastNames() {
        UserFilter userFilter = UserFilter.builder()
                .firstname("Petr")
                .lastname("Petrov")
                .build();

        Double actualResult = userQueryDsl.findAverageCountPizzaToOrderByFirstAndLastNames(session, userFilter);

        assertThat(actualResult).isEqualTo(1.5);
    }

    @Test
    void findPizzaNamesWithAvgCountToOrderOrderedByPizzaName() {
        List<Tuple> actualResult = userQueryDsl.findPizzaNamesWithAvgCountToOrderOrderedByPizzaName(session);

        assertThat(actualResult).hasSize(3);

        List<String> pizzaName = actualResult.stream().map(it -> it.get(0, String.class)).toList();
        assertThat(pizzaName).contains("Pepperoni", "Italian", "Four cheeses");
        List<Double> avgCountPizza = actualResult.stream().map(it -> it.get(1, Double.class)).toList();
        assertThat(avgCountPizza).contains(1.5, 2.5);
    }
}
