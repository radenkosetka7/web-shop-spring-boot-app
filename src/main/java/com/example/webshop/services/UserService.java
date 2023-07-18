package com.example.webshop.services;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.LoginResponse;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.entities.ProductEntity;
import com.example.webshop.models.requests.ChangePasswordRequest;
import com.example.webshop.models.requests.SignUpRequest;
import com.example.webshop.models.requests.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface UserService {

    Page<Product> getAllProductsForBuyer(Pageable page, Authentication authentication);

    Page<Product> getAllProductsForSeller(Pageable page,Integer finished, Authentication authentication);

    User findById(Integer id);

    User update(Integer id, UserRequest userRequest);

    User changePassword(Integer id, ChangePasswordRequest changePasswordRequest);

    LoginResponse findById(Integer id, Class<LoginResponse> response) throws NotFoundException;

    void signUp(SignUpRequest request);

    User activateAccount(String username);
}