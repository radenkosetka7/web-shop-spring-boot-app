package com.example.webshop.controllers;

import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.requests.AnswerRequest;
import com.example.webshop.models.requests.CommentRequest;
import com.example.webshop.models.requests.ProductRequest;
import com.example.webshop.models.requests.SearchRequest;
import com.example.webshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;


    @GetMapping
    public Page<Product> findAll(Pageable page,@RequestParam(required = false) String title)
    {
        return productService.findAll(page,title);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Integer id)
    {
        return productService.findById(id);
    }

    @PostMapping("/searchProducts")
    public Page<Product>  searchProducts(Pageable page,@RequestBody SearchRequest searchRequest)
    {
        return productService.searchProducts(page,searchRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product insert(@RequestBody ProductRequest productRequest, Authentication authentication)
    {
        return productService.insert(productRequest,authentication);
    }
    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment comment(@PathVariable Integer id,@RequestBody CommentRequest commentRequest,Authentication authentication)
    {
        return productService.commentProduct(id,commentRequest,authentication);
    }

    @PutMapping("/{id}/comment/answer")
    public Comment answer(@PathVariable Integer id,@RequestBody AnswerRequest answerRequest)
    {
        return productService.answer(id,answerRequest);
    }

    @PutMapping("/{id}")
    public Product purchaseProduct(@PathVariable Integer id,Authentication authentication)
    {
        return productService.purchaseProduct(id,authentication);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id)
    {
        productService.delete(id);
    }
}
