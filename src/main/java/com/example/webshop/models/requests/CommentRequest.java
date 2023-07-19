package com.example.webshop.models.requests;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank
    private String question;

}
