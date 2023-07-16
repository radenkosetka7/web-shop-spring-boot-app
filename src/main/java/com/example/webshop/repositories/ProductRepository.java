package com.example.webshop.repositories;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {

//    @Query("select p FROM ProductEntity p WHERE p.title LIKE :namePrefix%")
//    List<ProductEntity> findAllProductsByTitle(@Param("namePrefix") String namePrefix) throws NotFoundException;
}
