package com.techsolutio.products.init;

import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.dto.authentication.ApplicationUserCreateDTO;
import com.techsolutio.products.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    private final Logger log = LoggerFactory.getLogger(Initializer.class);

    private final UserDetailsServiceImpl userDetailsService;

    public Initializer(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void initApplication() {
        log.info("Initializer service.");

        ApplicationUserCreateDTO createDTO = new ApplicationUserCreateDTO();

        createDTO.setName("Matheus Brito");
        createDTO.setUsername("matheus");
        createDTO.setPassword("123456");

        userDetailsService.create(createDTO);
    }
}
