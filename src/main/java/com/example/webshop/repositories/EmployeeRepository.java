package com.example.webshop.repositories;

import com.example.webshop.models.entities.EmpoyeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmpoyeeEntity,Integer> {
}
