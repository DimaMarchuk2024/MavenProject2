package com.dima.mapper;

import com.dima.dto.DeliveryAddressReadDto;
import com.dima.dto.UserReadDto;
import com.dima.entity.DeliveryAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryAddressReadMapper implements Mapper<DeliveryAddress, DeliveryAddressReadDto> {

    private final UserReadMapper userReadMapper;

    @Override
    public DeliveryAddressReadDto map(DeliveryAddress deliveryAddress) {
        UserReadDto userReadDto = Optional.ofNullable(deliveryAddress.getUser())
                .map(userReadMapper::map).orElseThrow();

        return new DeliveryAddressReadDto(
                deliveryAddress.getId(),
                deliveryAddress.getAddress(),
                userReadDto
        );
    }
}