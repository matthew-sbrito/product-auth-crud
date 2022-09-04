package com.techsolutio.products.service;

import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.dto.authentication.*;
import com.techsolutio.products.exception.HttpResponseException;
import com.techsolutio.products.repository.ApplicationUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationUserRepository applicationUserRepository;

    public AuthenticationService(BCryptPasswordEncoder passwordEncoder, ApplicationUserRepository applicationUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserRepository = applicationUserRepository;
    }

    private ApplicationUser findById(Long id) throws HttpResponseException {
        return applicationUserRepository.findById(id)
                .orElseThrow(() -> new HttpResponseException(
                        HttpStatus.NOT_FOUND, String.format("User with id #%d not found!", id))
                );
    }

    public ApplicationUserDTO create(ApplicationUserCreateDTO params) {
        ApplicationUser user = new ApplicationUser();

        user.setName(params.getName());
        user.setUsername(params.getUsername());
        user.setPassword(passwordEncoder.encode(params.getPassword()));

        applicationUserRepository.saveAndFlush(user);

        return ApplicationUserDTO.fromDatabase(user);
    }

    public ApplicationUserDTO update(Long id, ApplicationUserUpdateDTO params) throws HttpResponseException {
        ApplicationUser user = findById(id);

        user.setName(params.getName());
        user.setUsername(params.getUsername());

        applicationUserRepository.saveAndFlush(user);

        return ApplicationUserDTO.fromDatabase(user);
    }

    public ApplicationUserDTO changePassword(Long id, ChangePasswordDTO params)
            throws HttpResponseException, AuthenticationException {
        ApplicationUser user = findById(id);

        if (!passwordEncoder.matches(params.getOldPassword(), user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Username/password invalid!");
        }

        user.setPassword(passwordEncoder.encode(params.getNewPassword()));

        applicationUserRepository.saveAndFlush(user);

        return ApplicationUserDTO.fromDatabase(user);
    }

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public ApplicationUserDTO authenticate(AuthenticateDTO params) {
        ApplicationUser user = applicationUserRepository.findByUsername(params.getUsername())
                .orElseThrow(() -> new HttpResponseException(
                        HttpStatus.NOT_FOUND, String.format("User with username #%s not found!", params.getUsername()))
                );

        if (!passwordEncoder.matches(params.getPassword(), user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Username/password invalid!");
        }

        return ApplicationUserDTO.fromDatabase(user);
    }
}
