package com.example.webshop.services;

import com.example.webshop.models.dto.Category;
import com.example.webshop.models.dto.CategoryDTO;
import com.example.webshop.models.dto.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    Page<Product> getAllProductsInCategory(Pageable page,Integer id);

    List<CategoryDTO> getAll();

    CategoryDTO findById(Integer id);
}
