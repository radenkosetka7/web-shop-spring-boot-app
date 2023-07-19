package com.example.webshop.models.requests;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String city;
    private String avatar;
    @NotBlank
    @Email
    private String mail;
}
