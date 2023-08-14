package com.example.webshop.controllers;

import com.example.webshop.models.dto.LoginResponse;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.requests.AccountActivationRequest;
import com.example.webshop.models.requests.LoginRequest;
import com.example.webshop.models.requests.SignUpRequest;
import com.example.webshop.services.AuthService;
import com.example.webshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }


    @PostMapping("sign-up")
    public void signUp(@RequestBody @Valid SignUpRequest request) {
        userService.signUp(request);
    }

    @PostMapping("uploadImage")
    public String uploadImage(@RequestParam(value = "file", required = false) MultipartFile file) {
       return userService.uploadImage(file);
    }

    @PostMapping("activeAccount")
    public User activeAccount(@RequestBody @Valid AccountActivationRequest accountActivationRequest) {
        if (authService.activateAccount(accountActivationRequest)) {
            return userService.activateAccount(accountActivationRequest.getUsername());
        }
        return null;
    }
}
