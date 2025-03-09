package com.dima.dao;

import com.dima.Enum.Role;
import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import com.dima.UserQueryDsl;
import com.dima.dao.impl.OrderDetailDao;
import com.dima.entity.Order;
import com.dima.entity.OrderDetail;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import com.dima.filter.PizzaFilter;
import com.dima.util.HibernateUtil;
import com.dima.util.TestDataBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderDetailDaoIT {

    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserQueryDsl userQueryDsl = UserQueryDsl.getInstance();
    private Session session;
    private OrderDetailDao orderDetailDao;

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        orderDetailDao = new OrderDetailDao(session);
        session.beginTransaction();
        TestDataBuilder.builderData(session);
    }

    @AfterEach
    void afterTest() {
        session.getTransaction().rollback();
        session.close();
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
    void findAll() {
        List<OrderDetail> actualResult = orderDetailDao.findAll();

        assertThat(actualResult).hasSize(6);
        List<BigDecimal> prices = actualResult.stream().map(OrderDetail::getPrice).toList();
        assertThat(prices).containsExactlyInAnyOrder(
                BigDecimal.valueOf(35),
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(90),
                BigDecimal.valueOf(70),
                BigDecimal.valueOf(25));
    }

    @Test
    void findById() {
        OrderDetail orderDetail = getOrderDetail();
        orderDetailDao.save(orderDetail);
        session.flush();
        session.clear();

        Optional<OrderDetail> actualResult = orderDetailDao.getById(orderDetail.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(orderDetail.getId());
    }

    @Test
    void save() {
        OrderDetail orderDetail = getOrderDetail();
        orderDetailDao.save(orderDetail);
        session.flush();
        session.clear();

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        OrderDetail orderDetail = getOrderDetail();
        orderDetailDao.save(orderDetail);
        session.flush();
        session.clear();
        OrderDetail orderDetail2 = getOrderDetail2(orderDetail.getId());
        orderDetailDao.update(orderDetail2);
        session.flush();
        session.clear();

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertThat(actualResult.getOrder()).isEqualTo(orderDetail2.getOrder());
        assertThat(actualResult.getPizzaToOrder()).isEqualTo(orderDetail2.getPizzaToOrder());
        assertThat(actualResult.getPrice()).isEqualTo(orderDetail2.getPrice());
    }

    @Test
    void delete(){
        OrderDetail orderDetail = getOrderDetail();
        orderDetailDao.save(orderDetail);
        session.flush();
        session.clear();
        orderDetailDao.delete(orderDetail);
        session.clear();

        OrderDetail actualResult = session.get(OrderDetail.class, orderDetail.getId());

        assertNull(actualResult);
    }

    private OrderDetail getOrderDetail() {
        PizzaToOrder pizzaToOrder = getPizzaToOrder();
        Order order = getOrder();

        return OrderDetail.builder()
                .order(order)
                .pizzaToOrder(pizzaToOrder)
                .price(BigDecimal.valueOf(40).setScale(2))
                .build();
    }

    private Order getOrder() {
        Order order = Order.builder()
                .dateTime(Instant.now().minus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .finalPrice(BigDecimal.valueOf(80).setScale(2))
                .build();
        session.persist(order);

        return order;
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

    private OrderDetail getOrderDetail2(Long id) {
        PizzaToOrder pizzaToOrder2 = getPizzaToOrder2();
        Order order2 = getOrder2();

        return OrderDetail.builder()
                .id(id)
                .order(order2)
                .pizzaToOrder(pizzaToOrder2)
                .price(BigDecimal.valueOf(50).setScale(2))
                .build();
    }

    private Order getOrder2() {
        Order order2 = Order.builder()
                .dateTime(Instant.now().minus(3, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .finalPrice(BigDecimal.valueOf(75).setScale(2))
                .build();
        session.persist(order2);

        return order2;
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
}