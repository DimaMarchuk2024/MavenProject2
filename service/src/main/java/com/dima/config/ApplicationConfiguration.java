package com.dima.config;

import com.dima.dao.DaoBase;
import com.dima.dao.impl.DeliveryAddressDao;
import com.dima.dao.impl.IngredientDao;
import com.dima.dao.impl.IngredientToOrderDao;
import com.dima.dao.impl.OrderDao;
import com.dima.dao.impl.OrderDetailDao;
import com.dima.dao.impl.PizzaDao;
import com.dima.dao.impl.PizzaIngredientDao;
import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.dao.impl.UserDao;
import com.dima.entity.DeliveryAddress;
import com.dima.entity.Ingredient;
import com.dima.entity.IngredientToOrder;
import com.dima.entity.Order;
import com.dima.entity.OrderDetail;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaIngredient;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import com.dima.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
@ComponentScan(basePackages = "com.dima")
public class ApplicationConfiguration {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    @Bean(destroyMethod = "close")
    public EntityManager session() {
        return (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory().getCurrentSession(), args1));
    }

    @Bean
    public DaoBase<Long, User> userDao() {
        return new UserDao(session());
    }

    @Bean
    public DaoBase<Long, DeliveryAddress> deliveryAddressDao() {
        return new DeliveryAddressDao(session());
    }

    @Bean
    public DaoBase<Integer, Ingredient> ingredientDao() {
        return new IngredientDao(session());
    }

    @Bean
    public DaoBase<Long, IngredientToOrder> ingredientToOrderDao() {
        return new IngredientToOrderDao(session());
    }

    @Bean
    public DaoBase<Long, Order> orderDao() {
        return new OrderDao(session());
    }

    @Bean
    public DaoBase<Long, OrderDetail> orderDetailDao() {
        return new OrderDetailDao(session());
    }

    @Bean
    public DaoBase<Integer, Pizza> pizzaDao() {
        return new PizzaDao(session());
    }

    @Bean
    public DaoBase<Integer, PizzaIngredient> pizzaIngredientDao() {
        return new PizzaIngredientDao(session());
    }

    @Bean
    public DaoBase<Long, PizzaToOrder> pizzaToOrderDao() {
        return new PizzaToOrderDao(session());
    }
}
