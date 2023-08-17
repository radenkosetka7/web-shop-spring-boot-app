package com.example.webshop.services.impl;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.AttributeValue;
import com.example.webshop.models.dto.Comment;
import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.entities.*;
import com.example.webshop.models.requests.AnswerRequest;
import com.example.webshop.models.requests.CommentRequest;
import com.example.webshop.models.requests.ProductRequest;
import com.example.webshop.models.requests.SearchRequest;
import com.example.webshop.repositories.*;
import com.example.webshop.services.LoggerService;
import com.example.webshop.services.ProductService;
import com.example.webshop.util.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final LoggerService loggerService;
    private final UserRepository userRepository;
    private final AttributeRepository attributeRepository;
    @Value("${productsDir:}")
    private String dir;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product insert(ProductRequest productRequest, Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        List<AttributeValueEntity> attributeValueEntities = new ArrayList<>();
        ProductEntity productEntity = modelMapper.map(productRequest, ProductEntity.class);
        productEntity.setId(null);
        productEntity.setFinished(0);
        productEntity.setCreationDate(new Date());
        productEntity.setUserSeller(userEntity);
        productEntity = productRepository.saveAndFlush(productEntity);
        for (ImageEntity imageEntity : productEntity.getImages()) {
            imageEntity.setId(null);
            imageEntity.setProduct(productEntity);
            imageRepository.saveAndFlush(imageEntity);
        }
        for (AttributeValueEntity attributeValueEntity : productEntity.getAttributeValues()) {
            AttributeValueEntityPK attributeValueEntityPK = new AttributeValueEntityPK();
            attributeValueEntityPK.setProductId(productEntity.getId());
            attributeValueEntityPK.setAttributeId(attributeValueEntity.getAttribute().getId());
            attributeValueEntity.setId(attributeValueEntityPK);
            attributeValueEntity.setProduct(productEntity);
            attributeValueRepository.saveAndFlush(attributeValueEntity);
        }
        loggerService.saveLog("The new product has been added by user " + userEntity.getUsername(), this.getClass().getName());
        return modelMapper.map(productEntity, Product.class);
    }

    @Override
    public Product findById(Integer id)
    {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(productEntity, Product.class);
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> files) {
        return Util.uploadProductImages(files,dir);
    }

    @Override
    public Page<Product> findAll(Pageable page, String title) {
        if (title == null || title.isEmpty()) {
            return productRepository.getAllActiveProducts(page).map(p -> modelMapper.map(p, Product.class));
        }
        else
        {
            return productRepository.searchAllByTitleIgnoreCase(page,title).map(p->modelMapper.map(p,Product.class));
        }
    }

    @Override
    public Comment commentProduct(Integer id,CommentRequest commentRequest, Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(NotFoundException::new);
        CommentEntity commentEntity = modelMapper.map(commentRequest, CommentEntity.class);
        commentEntity.setId(null);
        commentEntity.setDate(new Date());
        commentEntity.setUser(userEntity);
        commentEntity.setProduct(productEntity);
        loggerService.saveLog("The user " + userEntity.getUsername() + " has given comment to product " + commentEntity.getProduct().getTitle(), this.getClass().getName());
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        entityManager.refresh(commentEntity);
        return modelMapper.map(commentEntity, Comment.class);
    }

    @Override
    public Comment answer(Integer id, AnswerRequest answerRequest) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        commentEntity.setAnswer(answerRequest.getAnswer());
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        loggerService.saveLog("Product owner answered on comment for product  " + commentEntity.getProduct().getTitle(), this.getClass().getName());
        entityManager.refresh(commentEntity);
        return modelMapper.map(commentEntity, Comment.class);
    }

    @Override
    public Product purchaseProduct(Integer id, Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(NotFoundException::new);
        productEntity.setUserBuyer(userEntity);
        loggerService.saveLog("The user " + userEntity.getUsername() + " has purchased prdouct " + productEntity.getTitle(), this.getClass().getName());
        productEntity.setFinished(1);
        return modelMapper.map(productRepository.saveAndFlush(productEntity), Product.class);
    }

    @Override
    public Page<Product> searchProducts(Pageable page, SearchRequest searchRequest)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
        List<Predicate> predicates = new ArrayList<>();


        if (searchRequest.getCategoryName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("name"), searchRequest.getCategoryName()));
        }

        if (searchRequest.getLocation() != null) {
            predicates.add(criteriaBuilder.equal(root.get("city"), searchRequest.getLocation()));
        }

        if (searchRequest.getProductStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productStatus"), searchRequest.getProductStatus()));
        }

        if (searchRequest.getPriceFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(searchRequest.getPriceFrom())));
        }

        if (searchRequest.getPriceTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(searchRequest.getPriceTo())));
        }

        if (searchRequest.getAttributeValueList() != null && !searchRequest.getAttributeValueList().isEmpty()) {
            for (AttributeValue attributeValue : searchRequest.getAttributeValueList()) {
                predicates.add(criteriaBuilder.equal(
                        root.join("attributeValues").get("attribute").get("id"), attributeValue.getAttribute().getId()));
                predicates.add(criteriaBuilder.equal(
                        root.join("attributeValues").get("value"), attributeValue.getValue()));
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        List<ProductEntity> productEntities = typedQuery.getResultList();
        List<Product> products = productEntities.stream().map(e -> modelMapper.map(e, Product.class)).toList();

        return new PageImpl<>(products, page, products.size());
    }

    @Override
    public void delete(Integer id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(NotFoundException::new);
        productEntity.setFinished(2);
        loggerService.saveLog("The user has deleted product" + productEntity.getTitle(), this.getClass().getName());
        productRepository.saveAndFlush(productEntity);
    }
}
