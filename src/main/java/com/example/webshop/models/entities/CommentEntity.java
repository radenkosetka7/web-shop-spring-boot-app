package com.example.webshop.models.entities;

import javax.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "question", nullable = false, length = 255)
    private String question;
    @Basic
    @Column(name = "answer", nullable = true, length = 255)
    private String answer;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

}
