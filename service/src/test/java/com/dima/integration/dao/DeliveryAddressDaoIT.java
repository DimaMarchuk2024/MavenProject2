package com.dima.integration.dao;

import com.dima.dao.impl.DeliveryAddressDao;
import com.dima.entity.DeliveryAddress;
import com.dima.filter.UserFilter;
import com.dima.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
class DeliveryAddressDaoIT {

    private final DeliveryAddressDao deliveryAddressDao;

    @Test
    void findAllBy() {
        PageRequest pageable = PageRequest.of(0, 2);

        Page<DeliveryAddress> deliveryAddresses = deliveryAddressDao.findAllBy("k", pageable);

        assertThat(deliveryAddresses).hasSize(2);
    }

    @Test
    void findAllByUserFilter() {
        UserFilter userFilter = UserFilter.builder()
                .phoneNumber("11")
                .email("di")
                .build();

        List<DeliveryAddress> deliveryAddresses = deliveryAddressDao.findAllByUserFilter(userFilter);

        assertThat(deliveryAddresses).hasSize(3);
    }
}