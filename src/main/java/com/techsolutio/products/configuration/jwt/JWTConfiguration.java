package com.techsolutio.products.configuration.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
public class JWTConfiguration {
    // Chave secreta JWT da aplicação
    @Value("${jwt.secret}")
    private String secretKey;
    // Header d configuração token
    @NestedConfigurationProperty
    private final Header header = new Header();
    // Expiração do token de 2 horas
    private final int expiration = 2 * 60 * 60;

    // Rota para o login na aplicação.
    private final String loginURL = "/login";

    public static class Header {
        private final String name = "Authorization";
        private final String prefix = "Bearer";

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Header getHeader() {
        return header;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getLoginURL() {
        return loginURL;
    }
}
