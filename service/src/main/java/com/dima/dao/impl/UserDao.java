package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.User;
import org.hibernate.Session;

public class UserDao extends DaoBase<Long, User> {

    public UserDao(Session session) {
        super(User.class, session);
    }

}
