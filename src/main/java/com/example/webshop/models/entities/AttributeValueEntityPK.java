package com.example.webshop.models.entities;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class AttributeValueEntityPK implements Serializable {
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "attribute_id")
    private Integer attributeId;

}
