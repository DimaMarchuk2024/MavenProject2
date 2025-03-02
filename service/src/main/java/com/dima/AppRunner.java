package com.dima;

import com.dima.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AppRunner {

    public static void main(String[] args) {
        Session session;
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            session = sessionFactory.openSession();
            session.beginTransaction();

        }
    }
}