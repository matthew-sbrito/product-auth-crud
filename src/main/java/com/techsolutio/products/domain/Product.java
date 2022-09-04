package com.techsolutio.products.domain;

import javax.persistence.*;

@Entity
public class Product extends AbstractEntity {
    @Column(unique = true)
    private String name;

    @Column()
    private String provider;
}
