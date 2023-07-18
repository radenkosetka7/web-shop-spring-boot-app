package com.example.webshop.repositories;

import com.example.webshop.models.entities.ProductEntity;
import com.example.webshop.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    @Query("select p from ProductEntity p where p.userBuyer.id=:id")
    Page<ProductEntity> getAllProductsForBuyer(Pageable page,Integer id);

    @Query("select p from ProductEntity p where p.userSeller.id=:id and p.finished=:finished")
    Page<ProductEntity> getAllProductsForSeller(Pageable page,Integer id,Integer finished);

   boolean existsByMail(String mail);

    boolean existsByUsername(String username);

   Optional<UserEntity> findByUsernameAndStatus(String username,UserEntity.Status status);

   Optional<UserEntity> findByUsername(String username);

}
