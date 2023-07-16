package com.example.webshop.models.entities;

import com.example.webshop.models.enums.Type;
import javax.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "attribute")
public class AttributeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 45)
    private Type type;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity category;
    @OneToMany(mappedBy = "attribute")
    private List<AttributeValueEntity> attributeValues;

}
