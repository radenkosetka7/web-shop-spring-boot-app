package com.example.webshop.models.dto;

import lombok.Data;

@Data
public class Message {

    private Integer id;
    private String question;
    private Boolean status;
    private User user;
}
