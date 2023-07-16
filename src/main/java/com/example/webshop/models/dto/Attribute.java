package com.example.webshop.models.dto;

import com.example.webshop.models.enums.Type;
import lombok.Data;

@Data
public class Attribute {

    private Integer id;
    private String name;
    private Type type;
}
