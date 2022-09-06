package com.techsolutio.products.service;

import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.dto.authentication.*;
import com.techsolutio.products.exception.HttpResponseException;
import com.techsolutio.products.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository, @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Retriving user with username '{}'.", username);

        return applicationUserRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User with username '{}' not found.", username);

                    return new UsernameNotFoundException("User not found.");
                });
    }


    private ApplicationUser findById(Long id) throws HttpResponseException {
        return applicationUserRepository.findById(id)
                .orElseThrow(() -> new HttpResponseException(
                        HttpStatus.NOT_FOUND, String.format("User with id #%d not found.", id))
                );
    }

    public ApplicationUserDTO create(ApplicationUserCreateDTO params) {
        log.info("Creating user with username '{}'.", params.getUsername());

         Optional<ApplicationUser> userExists = applicationUserRepository.findByUsername(params.getUsername());

        if(userExists.isPresent()) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "Este usuário já existe!");
        }

        ApplicationUser user = new ApplicationUser();

        user.setName(params.getName());
        user.setUsername(params.getUsername());
        user.setPassword(passwordEncoder.encode(params.getPassword()));

        try {
            applicationUserRepository.saveAndFlush(user);

            log.info("User with username '{}' created successfully.", params.getUsername());

            return ApplicationUserDTO.fromDatabase(user);
        } catch (Exception ignored) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "There was an error creating user.");
        }
    }

    public ApplicationUserDTO update(Long id, ApplicationUserUpdateDTO params) throws HttpResponseException {
        log.info("Updating user with id #{}.", id);

        ApplicationUser user = findById(id);

        if(!Objects.equals(user.getUsername(), params.getUsername())) {
            Optional<ApplicationUser> userExists = applicationUserRepository.findByUsername(params.getUsername());

            if(userExists.isPresent()) {
                throw new HttpResponseException(HttpStatus.BAD_REQUEST, "Este usuário já existe!");
            }
        }

        user.setName(params.getName());
        user.setUsername(params.getUsername());

        try {
            applicationUserRepository.saveAndFlush(user);

            log.info("User with id #{} updated successfully.", id);

            return ApplicationUserDTO.fromDatabase(user);
        } catch (Exception ignored) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "There was an error updating user.");
        }
    }

    public ApplicationUserDTO changePassword(Long id, ChangePasswordDTO params) throws HttpResponseException {
        log.info("Changing password user with id #{}.", id);

        ApplicationUser user = findById(id);

        if (!passwordEncoder.matches(params.getOldPassword(), user.getPassword())) {
            throw new HttpResponseException(HttpStatus.UNAUTHORIZED, "Credênciais inválidas.");
        }

        try {
            user.setPassword(passwordEncoder.encode(params.getNewPassword()));

            applicationUserRepository.saveAndFlush(user);

            log.info("User with id #{} password changed successfully.", id);

            return ApplicationUserDTO.fromDatabase(user);
        } catch (Exception ignored) {
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "There was an error changing password user.");
        }
    }

    public ApplicationUser getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (ApplicationUser) auth.getPrincipal();
    }
}
