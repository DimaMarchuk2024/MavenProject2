package com.dima.dto;

import lombok.Value;

@Value
public class DeliveryAddressReadDto {

    Long id;
    String address;
    UserReadDto user;
}
