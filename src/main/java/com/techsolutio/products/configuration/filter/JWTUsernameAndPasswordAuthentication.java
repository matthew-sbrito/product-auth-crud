package com.techsolutio.products.configuration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techsolutio.products.configuration.jwt.JWTConfiguration;
import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.dto.authentication.ApplicationUserDTO;
import com.techsolutio.products.dto.authentication.AuthenticateResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techsolutio.products.configuration.jwt.JWTUtility;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTUsernameAndPasswordAuthentication extends UsernamePasswordAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(JWTUsernameAndPasswordAuthentication.class);
    private final JWTUtility jwtUtility;
    private final JWTConfiguration jwtConfiguration;
    private final AuthenticationManager authenticationManager;

    public JWTUsernameAndPasswordAuthentication(JWTUtility jwtUtility, JWTConfiguration jwtConfiguration, AuthenticationManager authenticationManager) {
        super(authenticationManager);

        this.jwtUtility = jwtUtility;
        this.jwtConfiguration = jwtConfiguration;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Tentativa de autenticação do usuario
     * @return Authentication
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Attempting authentication.");

        ApplicationUser applicationUser = null;

        try {
            applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(applicationUser == null){
            throw new UsernameNotFoundException("Unable to retrieve the username or password.");
        }

        log.info("Creating the authentication object for the user '{}' and calling UserDetailServiceImpl loadUserByUsername", applicationUser);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getAuthorities()
        );

        usernamePasswordAuthenticationToken.setDetails(applicationUser);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    /**
     * Gera a resposta de login do usuário que foi autenticado.
     * @return AuthenticateResponseDTO
     */
    private AuthenticateResponseDTO generateAuthResponse(Authentication authentication, String token) {
        log.info("Creating response auth for user.");
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();

        AuthenticateResponseDTO authenticateDTO = new AuthenticateResponseDTO();

        authenticateDTO.setExpireIn(jwtConfiguration.getExpiration());
        authenticateDTO.setUser(ApplicationUserDTO.fromDatabase(user));
        authenticateDTO.setToken(token);

        return authenticateDTO;
    }

    /*
    *  Usuário autenticado, gera o objeto de respota e envia ao usuário, pelo cabeçalho e corpo da resposta.
    */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        log.info("Authentication was successful for the user '{}', generating JWT token.", auth.getName());

        String encryptedToken = jwtUtility.generateToken((ApplicationUser) auth.getDetails());

        AuthenticateResponseDTO authenticateResponseDTO = generateAuthResponse(auth, encryptedToken);

        log.info("Token generated successfully, adding it to response.");

        response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, " + jwtConfiguration.getHeader().getName());
        response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + " " + encryptedToken);
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(authenticateResponseDTO.toJson());
        response.getWriter().flush();
    }
}
