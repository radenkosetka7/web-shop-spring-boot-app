package com.example.webshop.models.requests;

import javax.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Boolean productStatus;
    @NotBlank
    private String city;
    @NotBlank
    private String contact;
    @NotNull
    private Integer categoryId;
    @NotEmpty
    private List<ImageRequest> images;
    @NotEmpty
    private List<AttributeValueRequest> attributeValues;
}
