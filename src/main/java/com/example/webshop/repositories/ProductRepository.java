package com.example.webshop.repositories;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer>
{
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) and p.finished=0")
    Page<ProductEntity> searchAllByTitleIgnoreCase(Pageable page, String title);

    @Query("SELECT p from ProductEntity p where p.finished=0")
    Page<ProductEntity> getAllActiveProducts(Pageable page);
}
