package com.example.webshop.models.entities;

import com.example.webshop.models.enums.Role;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "employee")
public class EmpoyeeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "firstname", nullable = false, length = 45)
    private String firstname;
    @Basic
    @Column(name = "surname", nullable = false, length = 45)
    private String surname;
    @Basic
    @Column(name = "username", nullable = false, length = 45)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role", nullable = false)
    private Role role;
}
