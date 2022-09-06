package com.techsolutio.products.init;

import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.dto.ProductDTO;
import com.techsolutio.products.dto.authentication.ApplicationUserCreateDTO;
import com.techsolutio.products.service.ProductService;
import com.techsolutio.products.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Initializer {

    private final Logger log = LoggerFactory.getLogger(Initializer.class);

    private final UserDetailsServiceImpl userDetailsService;
    private final ProductService productService;

    public Initializer(UserDetailsServiceImpl userDetailsService, ProductService productService) {
        this.userDetailsService = userDetailsService;
        this.productService = productService;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void initApplication() {
        log.info("Initializer service.");

        ApplicationUserCreateDTO createDTO = new ApplicationUserCreateDTO();

        createDTO.setName("Matheus Brito");
        createDTO.setUsername("matheus");
        createDTO.setPassword("123456");

        userDetailsService.create(createDTO);

        List<ProductDTO> productDTOList = new ArrayList<>();

        productDTOList.add(new ProductDTO(null, "Carro branco", 25000D, "Fiat", null, null));
        productDTOList.add(new ProductDTO(null, "Carro preto", 27000D, "Fiat", null, null));
        productDTOList.add(new ProductDTO(null, "Moto 160", 17000D, "Honda", null, null));

        productDTOList.forEach(productService::create);
    }
}
