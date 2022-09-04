package com.techsolutio.products.dto;

public class ProductDTO {
    private Long id;
    private String nome;
    private String provider;

    public ProductDTO(Long id, String nome, String provider) {
        this.id = id;
        this.nome = nome;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
