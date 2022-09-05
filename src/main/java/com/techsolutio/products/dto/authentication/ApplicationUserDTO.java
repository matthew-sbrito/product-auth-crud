package com.techsolutio.products.dto.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techsolutio.products.domain.ApplicationUser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ApplicationUserDTO {

    private Long id;
    private String name;
    private String username;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date updatedAt;

    private ApplicationUserDTO() { }

    private ApplicationUserDTO(Long id, String name, String username, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ApplicationUserDTO fromDatabase(ApplicationUser applicationUser) {
        return new ApplicationUserDTO(
                applicationUser.getId(),
                applicationUser.getName(),
                applicationUser.getUsername(),
                Date.from(applicationUser.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(applicationUser.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
