package com.example.webshop.models.requests;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class AttributeValueRequest {

    @NotBlank
    private String value;
    @NotNull
    private Integer attributeId;

}
