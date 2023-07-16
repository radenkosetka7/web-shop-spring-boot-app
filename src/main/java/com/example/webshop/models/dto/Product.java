package com.example.webshop.models.dto;

import com.example.webshop.models.entities.CommentEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Product {

    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean productStatus;
    private String city;
    private String contact;
    private Date creationDate;
    private Boolean finished;
    private Category category;
    private User userSeller;
    private List<Image> images;
    private List<Comment> comments;
    private List<AttributeValue> attributeValues;
}
