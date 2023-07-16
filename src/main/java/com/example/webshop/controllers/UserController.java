package com.example.webshop.controllers;

import com.example.webshop.exceptions.ForbiddenException;
import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.requests.ChangePasswordRequest;
import com.example.webshop.models.requests.UserRequest;
import com.example.webshop.services.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id)
    {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Integer id, @Valid @RequestBody UserRequest userRequest,Authentication authentication)
    {
        //provjeri principala
        JwtUser user=(JwtUser)authentication.getPrincipal();
        if(!user.getId().equals(id))
        {
            throw new ForbiddenException();
        }
        return userService.update(id,userRequest);
    }

    @PutMapping("/{id}/changePassword")
    public User changePassword(@PathVariable Integer id, @Valid @RequestBody ChangePasswordRequest changePasswordRequest,Authentication authentication)
    {
        //provjeri principala
        JwtUser user=(JwtUser)authentication.getPrincipal();
        if(!user.getId().equals(id))
        {
            throw new ForbiddenException();
        }
        return userService.changePassword(id,changePasswordRequest);
    }

    @GetMapping("/{id}/products/purchased")
    public Page<Product> getAllProductsForBuyer(Pageable page, Authentication authentication)
    {
        return userService.getAllProductsForBuyer(page,authentication);
    }
    @GetMapping("/{id}/products/sold")
    public Page<Product> getAllProductsForSeller(Pageable page,Boolean finished,Authentication authentication)
    {
        return userService.getAllProductsForSeller(page,finished,authentication);
    }
}
