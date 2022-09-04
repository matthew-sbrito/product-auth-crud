package com.techsolutio.products.configuration.filter;

import com.techsolutio.products.configuration.jwt.JWTConfiguration;
import com.techsolutio.products.configuration.jwt.JWTUtility;
import com.techsolutio.products.domain.ApplicationUser;
import com.techsolutio.products.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTTokenAuthorizationFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(JWTTokenAuthorizationFilter.class);

    private final JWTUtility jwtUtility;
    private final JWTConfiguration jwtConfiguration;
    private final UserDetailsServiceImpl userDetailsService;

    public JWTTokenAuthorizationFilter(JWTUtility jwtUtility, JWTConfiguration jwtConfiguration, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtility = jwtUtility;
        this.jwtConfiguration = jwtConfiguration;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Recebe o nome de usuário, pega no banco e retorna o authentication
     * @return UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken(String username) {
        ApplicationUser user = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );

        auth.setDetails(user);

        return auth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(jwtConfiguration.getHeader().getName());

        if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
        String username = jwtUtility.getUsernameFromToken(token);

        log.info("user with username '{}' has made a request", username);

        UsernamePasswordAuthenticationToken auth = usernamePasswordAuthenticationToken(username);

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
