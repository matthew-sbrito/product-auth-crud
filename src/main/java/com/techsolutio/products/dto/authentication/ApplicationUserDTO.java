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
    private Date createAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date updateAt;

    private ApplicationUserDTO() { }

    private ApplicationUserDTO(Long id, String name, String username, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.createAt = Date.from(createAt.atZone(ZoneId.systemDefault()).toInstant());
        this.updateAt = Date.from(updateAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static ApplicationUserDTO fromDatabase(ApplicationUser applicationUser) {
        return new ApplicationUserDTO(
                applicationUser.getId(),
                applicationUser.getName(),
                applicationUser.getUsername(),
                applicationUser.getCreatedAt(),
                applicationUser.getUpdatedAt()
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

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
