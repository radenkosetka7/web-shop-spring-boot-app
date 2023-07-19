package com.example.webshop.models.entities;

import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "message")
public class MessageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "question", nullable = false, length = 255)
    private String question;
    @Basic
    @Column(name = "status", nullable = false)
    private Boolean status;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

}
