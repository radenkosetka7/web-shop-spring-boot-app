package com.example.webshop.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {

    private List<String> methods;
    private String pattern;
    private List<String> roles;
}
