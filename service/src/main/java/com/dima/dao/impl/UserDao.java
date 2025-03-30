package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends DaoBase<Long, User> {

    public UserDao(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
