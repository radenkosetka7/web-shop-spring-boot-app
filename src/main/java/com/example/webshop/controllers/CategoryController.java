package com.example.webshop.controllers;

import com.example.webshop.models.dto.CategoryDTO;
import com.example.webshop.models.dto.Product;
import com.example.webshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}/products")
    public Page<Product> findAllProductsInCategory(Pageable page, @PathVariable Integer id) {
        return categoryService.getAllProductsInCategory(page, id);
    }

    @GetMapping
    public List<CategoryDTO> getAll()
    {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategory(@PathVariable Integer id)
    {
        return categoryService.findById(id);
    }

}
