package com.dima.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

@Value
@FieldNameConstants
public class PizzaCreateEditDto {

    String name;
    MultipartFile image;
}
