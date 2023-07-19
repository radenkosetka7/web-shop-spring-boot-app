package com.example.webshop.models.entities;

import javax.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@Entity
@Table(name = "attribute_value")
public class AttributeValueEntity {
    @EmbeddedId
    private AttributeValueEntityPK id;
    @Basic
    @Column(name = "value", nullable = false, length = 45)
    private String value;
    @MapsId("productId")
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;
    @MapsId("attributeId")
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", nullable = false)
    private AttributeEntity attribute;

}
