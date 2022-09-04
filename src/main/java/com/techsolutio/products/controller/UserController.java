package com.techsolutio.products.controller;

import com.techsolutio.products.dto.authentication.ApplicationUserCreateDTO;
import com.techsolutio.products.dto.authentication.ApplicationUserDTO;
import com.techsolutio.products.dto.authentication.ApplicationUserUpdateDTO;
import com.techsolutio.products.dto.authentication.ChangePasswordDTO;
import com.techsolutio.products.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public UserController(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ApplicationUserDTO> registerUser(@RequestBody ApplicationUserCreateDTO params) {
        log.info("Request for register user in application.");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDetailsServiceImpl.create(params));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody ApplicationUserUpdateDTO params
    ) {
        log.info("Request for update user in application.");

        return ResponseEntity
                .ok(userDetailsServiceImpl.update(id, params));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<ApplicationUserDTO> updatePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordDTO params
    ) {
        log.info("Request for change password user in application.");

        return ResponseEntity
                .ok(userDetailsServiceImpl.changePassword(id, params));
    }
}
