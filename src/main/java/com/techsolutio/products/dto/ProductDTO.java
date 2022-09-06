package com.techsolutio.products.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techsolutio.products.domain.Product;

import java.time.ZoneId;
import java.util.Date;

public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private String provider;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date updatedAt;

    public ProductDTO(Long id, String name, Double price, String provider, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductDTO fromDatabase(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getProvider(),
                Date.from(product.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(product.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
