package com.example.webshop.models.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic
    @Column(name = "surname", nullable = false, length = 45)
    private String surname;
    @Basic
    @Column(name = "city", nullable = false, length = 45)
    private String city;
    @Basic
    @Column(name = "username", nullable = false, length = 45)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Basic
    @Column(name = "avatar", nullable = true, length = 1024)
    private String avatar;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private Status status;
    @Basic
    @Column(name = "mail", nullable = false, length = 45)
    private String mail;
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<CommentEntity> comments;
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<MessageEntity> messages;
    @OneToMany(mappedBy = "userSeller")
    @ToString.Exclude
    private List<ProductEntity> productsSeller;
    @OneToMany(mappedBy = "userBuyer")
    @ToString.Exclude
    private List<ProductEntity> productsBuyer;

    public enum Status {
        REQUESTED, ACTIVE, BLOCKED
    }

}
