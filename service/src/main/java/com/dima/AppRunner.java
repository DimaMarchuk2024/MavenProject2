package com.dima;

import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.dao.impl.UserDao;
import com.dima.entity.User;
import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Proxy;
import java.util.List;

public class AppRunner {

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
            session.beginTransaction();

//            UserDao userDao = new UserDao(session);
//            List<User> users = userDao.findAll();
//            User ivan = users.stream().filter(user -> user.getPhoneNumber().equals("11-11-111")).toList().get(0);
//            User user = userDao.getById(1L).get();
//            System.out.println(user);

            session.getTransaction().commit();

        }

    }
}