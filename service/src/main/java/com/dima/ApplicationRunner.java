package com.dima;

import com.dima.dao.impl.DeliveryAddressDao;
import com.dima.dao.impl.UserDao;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApplicationRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);

            EntityManager entityManager = context.getBean(EntityManager.class);
            System.out.println(entityManager);
            UserDao userDao = context.getBean(UserDao.class);
            DeliveryAddressDao deliveryAddressDao = context.getBean(DeliveryAddressDao.class);
            System.out.println(userDao);
    }
}