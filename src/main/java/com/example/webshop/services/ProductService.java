package com.example.webshop.services;

import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.requests.AnswerRequest;
import com.example.webshop.models.requests.CommentRequest;
import com.example.webshop.models.requests.ProductRequest;
import com.example.webshop.models.requests.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product insert(ProductRequest productRequest, Authentication authentication);

    Product findById(Integer id);

    Page<Product> findAll(Pageable page,String title);

    List<String> uploadImages(List<MultipartFile> files);


    Comment commentProduct(Integer id,CommentRequest commentRequest,Authentication authentication);

    Comment answer(Integer id, AnswerRequest answerRequest);

    Product purchaseProduct(Integer id,Authentication authentication);

    Page<Product> searchProducts(Pageable page, SearchRequest searchRequest);

    void delete(Integer id);
}
