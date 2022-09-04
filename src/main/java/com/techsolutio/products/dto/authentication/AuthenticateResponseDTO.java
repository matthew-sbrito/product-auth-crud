package com.techsolutio.products.dto.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class AuthenticateResponseDTO {
    ApplicationUserDTO user;
    String token;
    long expireIn;
    long createIn;

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

    public long getCreateIn() {
        return createIn;
    }

    public void setCreateIn(long createIn) {
        this.createIn = createIn;
    }
}
