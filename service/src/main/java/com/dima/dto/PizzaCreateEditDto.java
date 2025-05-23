package com.dima.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

@Value
@FieldNameConstants
public class PizzaCreateEditDto {

    @NotBlank
    @Size(min = 3, max = 64)
    String name;

    MultipartFile image;
}
