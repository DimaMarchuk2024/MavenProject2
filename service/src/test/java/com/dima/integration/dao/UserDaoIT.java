package com.dima.integration.dao;

import com.dima.dao.impl.UserDao;
import com.dima.entity.User;
import com.dima.filter.UserFilter;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
class UserDaoIT {

    private final UserDao userDao;

    @Test
    void findAllByOrderFinalPrice() {
        List<User> allByOrderFinalPrice = userDao.findAllByOrderFinalPrice(BigDecimal.valueOf(89).setScale(2));

        assertThat(allByOrderFinalPrice).hasSize(2);
    }

    @Test
    void findAllByPizzaName() {
        List<User> allByPizzaName = userDao.findAllByPizzaName("it");

        assertThat(allByPizzaName).hasSize(2);
    }

    @Test
    void finAllByFilter() {
        UserFilter filter = UserFilter.builder()
                .firstname("im")
                .lastname("mov")
                .birthDate(LocalDate.of(1996, 5, 15))
                .phoneNumber("3")
                .email("di")
                .build();

        List<User> users = userDao.findAllByFilter(filter);

        assertThat(users).hasSize(1);
    }
}