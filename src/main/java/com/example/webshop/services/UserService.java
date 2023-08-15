package com.example.webshop.services;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.LoginResponse;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.requests.ChangePasswordRequest;
import com.example.webshop.models.requests.SignUpRequest;
import com.example.webshop.models.requests.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    Page<Product> getAllProductsForBuyer(Pageable page, Authentication authentication,String title);

    Page<Product> getAllProductsForSeller(Pageable page,Integer finished, Authentication authentication,String title);

    User findById(Integer id);

    User update(Integer id, UserRequest userRequest);

    String uploadImage(MultipartFile file);

    User changePassword(Integer id, ChangePasswordRequest changePasswordRequest);

    LoginResponse findById(Integer id, Class<LoginResponse> response) throws NotFoundException;

    void signUp(SignUpRequest request);

    User activateAccount(String username);
}