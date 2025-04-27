package com.dima.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class DeliveryAddressCreateEditDto {

    String address;
    Long userId;
}
