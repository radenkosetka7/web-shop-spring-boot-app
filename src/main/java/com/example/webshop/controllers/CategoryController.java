package com.example.webshop.controllers;

import com.example.webshop.models.dto.Product;
import com.example.webshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public Page<Product> findAllProductsInCategory(Pageable page, @PathVariable Integer id) {
        return categoryService.getAllProductsInCategory(page, id);
    }

}
