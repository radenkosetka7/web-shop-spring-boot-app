package com.example.webshop.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Integer id;
    private String question;
    private String answer;
    private User user;
    private Date date;
}
