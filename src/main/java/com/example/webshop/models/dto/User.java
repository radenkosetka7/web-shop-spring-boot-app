package com.example.webshop.models.dto;

import com.example.webshop.models.enums.UserStatus;
import lombok.Data;

@Data
public class User {

    private Integer id;
    private String name;
    private String surname;
    private String city;
    private String username;
    private String avatar;
    private String mail;
    private UserStatus status;
}
