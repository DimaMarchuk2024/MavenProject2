package com.dima.mapper;

import com.dima.dao.impl.UserDao;
import com.dima.dto.DeliveryAddressCreateEditDto;
import com.dima.entity.DeliveryAddress;
import com.dima.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryAddressCreateEditMapper implements Mapper<DeliveryAddressCreateEditDto, DeliveryAddress> {

    private final UserDao userDao;

    @Override
    public DeliveryAddress map(DeliveryAddressCreateEditDto fromObject, DeliveryAddress toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public DeliveryAddress map(DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        copy(deliveryAddressCreateEditDto, deliveryAddress);

        return deliveryAddress;
    }

    private void copy(DeliveryAddressCreateEditDto deliveryAddressCreateEditDto, DeliveryAddress deliveryAddress) {
        deliveryAddress.setAddress(deliveryAddressCreateEditDto.getAddress());
        deliveryAddress.setUser(getUser(deliveryAddressCreateEditDto.getUserId()));
    }

    public User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userDao::findById)
                .orElse(null);
    }
}

