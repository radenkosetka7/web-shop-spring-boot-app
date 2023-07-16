package com.example.webshop.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    @Basic
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Basic
    @Column(name = "price", nullable = false, precision = 2)
    private BigDecimal price;
    @Basic
    @Column(name = "product_status", nullable = false)
    private Boolean productStatus;
    @Basic
    @Column(name = "city", nullable = false, length = 45)
    private String city;
    @Basic
    @Column(name = "contact", nullable = false, length = 45)
    private String contact;
    @Basic
    @Column(name = "finished", nullable = false)
    private Integer finished;
    @Basic
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
    @OneToMany(mappedBy = "product")
    private List<AttributeValueEntity> attributeValues;
    @OneToMany(mappedBy = "product")
    private List<CommentEntity> comments;
    @OneToMany(mappedBy = "product")
    private List<ImageEntity> images;
    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    private UserEntity userSeller;
    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", nullable = false)
    private UserEntity userBuyer;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity category;

}
