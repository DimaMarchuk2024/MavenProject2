package com.dima;

import com.dima.config.ApplicationConfiguration;
import com.dima.dao.impl.DeliveryAddressDao;
import com.dima.dao.impl.UserDao;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppRunner {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {

            EntityManager entityManager = context.getBean(EntityManager.class);
            System.out.println(entityManager);
            UserDao userDao = context.getBean(UserDao.class);
            DeliveryAddressDao deliveryAddressDao = context.getBean(DeliveryAddressDao.class);
            System.out.println(userDao);
        }
    }
}