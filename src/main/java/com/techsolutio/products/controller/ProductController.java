package com.techsolutio.products.controller;

import com.techsolutio.products.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findProductList() {
        return ResponseEntity.ok(List.of());
    }
}
