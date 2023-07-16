package com.example.webshop.models.requests;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class ImageRequest {
    @NotBlank
    private String productImage;

}
