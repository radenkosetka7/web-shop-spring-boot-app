package com.example.webshop.repositories;

import com.example.webshop.models.entities.LoggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerRepository extends JpaRepository<LoggerEntity,Integer> {
}
