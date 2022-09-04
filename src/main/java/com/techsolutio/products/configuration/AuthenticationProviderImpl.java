package com.techsolutio.products.configuration;

import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationProviderImpl(AuthenticationService authenticationService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        ApplicationUser user = authenticationService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Username/password invalid!");
        }

        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
