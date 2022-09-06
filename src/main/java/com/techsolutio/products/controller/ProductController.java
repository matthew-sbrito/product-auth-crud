package com.techsolutio.products.controller;

import com.techsolutio.products.dto.ProductDTO;
import com.techsolutio.products.service.ProductService;
import com.techsolutio.products.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final UserDetailsServiceImpl userDetailsService;

    public ProductController(ProductService productService, UserDetailsServiceImpl userDetailsService) {
        this.productService = productService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findProductList(
            @PageableDefault Pageable pageable
    ) {
        log.info("Request find product list.");

        return ResponseEntity.ok(productService.list(pageable));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestBody ProductDTO params
    ) {
        log.info("Request create product.");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.create(params));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO params
    ) {
        log.info("Request update product by user '{}'.", userDetailsService.getLoggedUser().getUsername());

        return ResponseEntity.ok(productService.update(id, params));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        log.info("Request delete product by user '{}'.", userDetailsService.getLoggedUser().getUsername());

        productService.delete(id);

        return ResponseEntity.ok().build();
    }
}
