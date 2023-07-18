package com.example.webshop.services.impl;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.entities.CategoryEntity;
import com.example.webshop.repositories.CategoryRepository;
import com.example.webshop.services.CategoryService;
import com.example.webshop.services.LoggerService;
import com.example.webshop.util.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final LoggerService loggerService;
    private final ModelMapper modelMapper;

    @Override
    public Page<Product> getAllProductsInCategory(Pageable page,Integer id) {

        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(NotFoundException::new);
        List<Product> productList = categoryEntity.getProducts().stream().map(p->modelMapper.map(p,Product.class)).collect(Collectors.toList());
        loggerService.saveLog("Get all products in given category ",this.getClass().getName());
        return Util.getPage(page,productList);
    }
}
