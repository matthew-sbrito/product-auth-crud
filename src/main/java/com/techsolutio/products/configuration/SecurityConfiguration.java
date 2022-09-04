package com.techsolutio.products.configuration;

import com.techsolutio.products.configuration.filter.JWTTokenAuthorizationFilter;
import com.techsolutio.products.configuration.filter.JWTUsernameAndPasswordAuthentication;
import com.techsolutio.products.configuration.jwt.JWTConfiguration;
import com.techsolutio.products.configuration.jwt.JWTUtility;
import com.techsolutio.products.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtility jwtUtility;
    private final JWTConfiguration jwtConfiguration;
    private final AuthenticationConfiguration configuration;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, JWTUtility jwtUtility, JWTConfiguration jwtConfiguration, AuthenticationConfiguration configuration) {
        this.userDetailsService = userDetailsService;
        this.jwtUtility = jwtUtility;
        this.jwtConfiguration = jwtConfiguration;
        this.configuration = configuration;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JWTUsernameAndPasswordAuthentication filterGenerateJWT = new JWTUsernameAndPasswordAuthentication(
                jwtUtility, jwtConfiguration, authenticationManager()
        );

        JWTTokenAuthorizationFilter filterParserJWT = new JWTTokenAuthorizationFilter(
                jwtUtility, jwtConfiguration, userDetailsService
        );

        http
                .csrf().disable()
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .and().addFilter(filterGenerateJWT)
                .addFilterAfter(filterParserJWT, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .authenticationManager(authenticationManager())
                .authorizeRequests()
                .antMatchers(jwtConfiguration.getLoginURL(), "/user/register").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
