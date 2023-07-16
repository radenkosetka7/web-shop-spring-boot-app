package com.example.webshop.repositories;

import com.example.webshop.models.entities.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity,Integer> {
}
