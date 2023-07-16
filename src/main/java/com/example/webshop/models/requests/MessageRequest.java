package com.example.webshop.models.requests;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class MessageRequest {

    @NotBlank
    private String question;
    @NotNull
    private Boolean status;
}
