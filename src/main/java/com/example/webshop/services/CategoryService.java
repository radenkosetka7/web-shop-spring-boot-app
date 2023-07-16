package com.example.webshop.services;

import com.example.webshop.models.dto.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<Product> getAllProductsInCategory(Pageable page,Integer id);
}
