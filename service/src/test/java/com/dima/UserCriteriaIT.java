package com.dima;

import com.dima.entity.Order;
import com.dima.entity.OrderDetail;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCriteriaIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserCriteria userCriteria = UserCriteria.getInstance();
    private Session session = null;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
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
        List<User> actualResult = userCriteria.findAll(session);

        assertThat(actualResult).hasSize(3);
        List<String> firstNames = actualResult.stream().map(User::getFirstname).toList();
        assertThat(firstNames).containsExactlyInAnyOrder("Ivan","Petr", "Dima");
    }

    @Test
    void findAllByFirstName() {
        List<User> actualResult = userCriteria.findAllByFirstName(session, "Ivan");

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getFirstname()).isEqualTo("Ivan");
    }

    @Test
    void findLimitedUsersOrderedByBirthday() {
        int limit = 2;
        List<User> actualResult = userCriteria.findLimitedUsersOrderedByBirthday(session, limit);

        assertThat(actualResult).hasSize(limit);
        List<String> firstNames = actualResult.stream().map(User::getFirstname).toList();
        assertThat(firstNames).contains("Dima", "Ivan");
    }

    @Test
    void findAllPizzaToOrderByPizzaName() {
        List<PizzaToOrder> actualResult = userCriteria.findAllPizzaToOrderByPizzaName(session, "Pepperoni");

        assertThat(actualResult).hasSize(2);
        List<User> users = actualResult.stream().map(PizzaToOrder::getUser).toList();
        List<String> lastnames = users.stream().map(User::getLastname).toList();
        assertThat(lastnames).contains("Petrov", "Dimov");
    }

    @Test
    void findAllOrdersByPizzaName() {
        List<OrderDetail> actualResult = userCriteria.findAllOrdersByPizzaName(session, "Italian");

        assertThat(actualResult).hasSize(2);
        List<Order> orders = actualResult.stream().map(OrderDetail::getOrder).toList();
        List<BigDecimal> finalPrices = orders.stream().map(Order::getFinalPrice).toList();
        assertThat(finalPrices).contains(BigDecimal.valueOf(85),BigDecimal.valueOf(95));
    }

    @Test
    void findAverageCountPizzaToOrderByFirstAndLastNames() {
        Double actualResult = userCriteria.findAverageCountPizzaToOrderByFirstAndLastNames(session, "Petr", "Petrov");

        assertThat(actualResult).isEqualTo(1.5);
    }

    @Test
    void findPizzaNamesWithAvgCountToOrderOrderedByPizzaName() {
        List<Object[]> actualResult = userCriteria.findPizzaNamesWithAvgCountToOrderOrderedByPizzaName(session);

        assertThat(actualResult).hasSize(3);
        List<String> pizzaName = actualResult.stream().map(a -> (String) a[0]).toList();
        assertThat(pizzaName).contains("Pepperoni", "Italian", "Four cheeses");
        List<Double> avgCountPizza = actualResult.stream().map(a -> (Double) a[1]).toList();
        assertThat(avgCountPizza).contains(1.5, 2.5);
    }
}
