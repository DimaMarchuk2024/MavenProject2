package com.dima.dao.impl;

import com.dima.entity.User;
import com.dima.filter.UserFilter;

import java.util.List;

public interface FilterUserDao {

    List<User> findAllByFilter(UserFilter userFilter);
}
