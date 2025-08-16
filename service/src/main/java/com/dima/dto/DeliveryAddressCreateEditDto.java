package com.dima.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class DeliveryAddressCreateEditDto {

    @NotBlank
    @Size(min = 2, max = 128)
    String address;

    Long userId;
}
