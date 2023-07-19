package com.example.webshop.models.requests;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;

}
