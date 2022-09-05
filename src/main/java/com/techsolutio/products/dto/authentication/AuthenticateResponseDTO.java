package com.techsolutio.products.dto.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class AuthenticateResponseDTO {
    private ApplicationUserDTO user;
    private String token;
    private long expireIn;
    private String type = "Bearer";

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public ApplicationUserDTO getUser() {
        return user;
    }

    public void setUser(ApplicationUserDTO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }

    public String getType() {
        return type;
    }
}
