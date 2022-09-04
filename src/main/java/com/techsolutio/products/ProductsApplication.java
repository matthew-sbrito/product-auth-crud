package com.techsolutio.products;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.techsolutio.products.configuration.jwt.JWTConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class ProductsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductsApplication.class, args);
	}

}
