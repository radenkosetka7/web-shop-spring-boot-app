package com.example.webshop.services.impl;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.entities.*;
import com.example.webshop.models.requests.AnswerRequest;
import com.example.webshop.models.requests.CommentRequest;
import com.example.webshop.models.requests.ProductRequest;
import com.example.webshop.repositories.*;
import com.example.webshop.services.ProductService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.webshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final AttributeValueRepository attributeValueRepository;
    //private final Authentication authentication;
    private final UserRepository userRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product insert(ProductRequest productRequest,Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UserEntity userEntity=userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        ProductEntity productEntity = modelMapper.map(productRequest, ProductEntity.class);
        productEntity.setId(null);
        productEntity.setFinished(0);
        productEntity.setCreationDate(new Date());
        productEntity.setUserSeller(userEntity);
        //izvuci principala i postavi ga na seller-a
        productEntity = productRepository.saveAndFlush(productEntity);
        entityManager.refresh(productEntity);
        for (ImageEntity imageEntity : productEntity.getImages()) {
            imageEntity.setId(null);
            imageEntity.setProduct(productEntity);
            imageRepository.saveAndFlush(imageEntity);
        }
        for (AttributeValueEntity attributeValueEntity : productEntity.getAttributeValues()) {
            AttributeValueEntityPK attributeValueEntityPK = new AttributeValueEntityPK();
            attributeValueEntityPK.setProductId(attributeValueEntity.getProduct().getId());
            attributeValueEntityPK.setAttributeId(attributeValueEntity.getAttribute().getId());
            attributeValueEntity.setId(attributeValueEntityPK);
            attributeValueRepository.saveAndFlush(attributeValueEntity);
        }
        return modelMapper.map(productEntity,Product.class);
    }

    @Override
    public Product findById(Integer id) {
        return modelMapper.map(productRepository.findById(id), Product.class);
    }

    @Override
    public Page<Product> findAll(Pageable page) {
        return productRepository.findAll(page).map(p -> modelMapper.map(p, Product.class));
    }

    @Override
    public Comment commentProduct(CommentRequest commentRequest,Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UserEntity userEntity=userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        CommentEntity commentEntity = modelMapper.map(commentRequest, CommentEntity.class);
        commentEntity.setId(null);
        commentEntity.setDate(new Date());
        commentEntity.setUser(userEntity);
        //iz principala uzeti usera
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        entityManager.refresh(commentEntity);
        return modelMapper.map(commentEntity, Comment.class);
    }

    @Override
    public Comment answer(Integer id, AnswerRequest answerRequest) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        commentEntity.setAnswer(answerRequest.getAnswer());
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        entityManager.refresh(commentEntity);
        return modelMapper.map(commentEntity, Comment.class);
    }

    @Override
    public Product purchaseProduct(Integer id,Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UserEntity userEntity=userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(NotFoundException::new);
        productEntity.setUserSeller(userEntity);
        //iz principala izvuci usera
        productEntity.setFinished(1);
        return modelMapper.map(productRepository.saveAndFlush(productEntity), Product.class);
    }

    @Override
    public void delete(Integer id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(NotFoundException::new);
        productEntity.setFinished(2);
        productRepository.saveAndFlush(productEntity);
    }
}
