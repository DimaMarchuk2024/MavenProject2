package com.dima.integration.dao;

import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.entity.PizzaToOrder;
import com.dima.filter.PizzaFilter;
import com.dima.filter.UserFilter;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
class PizzaToOrderDaoIT {

    private final PizzaToOrderDao pizzaToOrderDao;

    @Test
    void findAllByPizzaFilter() {
        PizzaFilter pizzaFilter = PizzaFilter.builder()
                .pizzaName("pep")
                .build();

        List<PizzaToOrder> allByPizzaFilter = pizzaToOrderDao.findAllByPizzaFilter(pizzaFilter);

        assertThat(allByPizzaFilter).hasSize(2);
    }

    @Test
    void findAllByUserFilter() {
        UserFilter userFilter = UserFilter.builder()
                .firstname("im")
                .lastname("mov")
                .birthDate(LocalDate.of(1996, 5, 15))
                .phoneNumber("3")
                .email("di")
                .build();

        List<PizzaToOrder> allByUserFilter = pizzaToOrderDao.findAllByUserFilter(userFilter);

        assertThat(allByUserFilter).hasSize(4);
    }
}