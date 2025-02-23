package com.dima;

import com.dima.entity.Pizza;
import com.dima.entity.PizzaIngredient;
import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppServiceTest {

    @Test
    void checkH2() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Pizza pizza = Pizza.builder()
                    .name("Pepperoni")
                    .build();

            session.persist(pizza);
            session.get(Pizza.class, pizza.getId());

            session.getTransaction().rollback();
        }
    }
}
