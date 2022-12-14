package com.techsolutio.products.service;

import com.techsolutio.products.domain.Product;
import com.techsolutio.products.dto.ProductDTO;
import com.techsolutio.products.exception.HttpResponseException;
import com.techsolutio.products.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new HttpResponseException(HttpStatus.NOT_FOUND, "Product not found!")
        );
    }

    public Page<ProductDTO> list(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductDTO::fromDatabase);
    }

    public ProductDTO create(ProductDTO params) {
        log.info("Creating product with name '{}'", params.getName());

        Product product = new Product();

        product.setName(params.getName());
        product.setProvider(params.getProvider());
        product.setPrice(params.getPrice());

        try {

            productRepository.saveAndFlush(product);

            log.info("Product created successfully.");

            return ProductDTO.fromDatabase(product);
        } catch (Exception ignored) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "There was an error creating product");
        }
    }

    public ProductDTO update(Long id, ProductDTO params) {
        log.info("Updating product with id #{}", id);

        Product product = findById(id);

        product.setName(params.getName());
        product.setProvider(params.getProvider());
        product.setPrice(params.getPrice());

        try {
            productRepository.saveAndFlush(product);

            log.info("Product updated successfully.");

            return ProductDTO.fromDatabase(product);
        }catch (Exception ignored) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "There was an error updating product");
        }
    }

    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }
}
