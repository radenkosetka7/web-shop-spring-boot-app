package com.example.webshop.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO extends Category{

    private List<Attribute> attributes;
}
