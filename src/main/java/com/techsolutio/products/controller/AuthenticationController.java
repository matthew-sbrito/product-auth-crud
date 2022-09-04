package com.techsolutio.products.controller;

import com.techsolutio.products.dto.authentication.ApplicationUserCreateDTO;
import com.techsolutio.products.dto.authentication.ApplicationUserDTO;
import com.techsolutio.products.dto.authentication.AuthenticateDTO;
import com.techsolutio.products.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationUserDTO> createApplicationUser(@RequestBody ApplicationUserCreateDTO params) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.create(params));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApplicationUserDTO> authenticate(
            @RequestBody AuthenticateDTO params
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(params));
    }

}
