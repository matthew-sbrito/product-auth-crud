package com.techsolutio.products.dto.authentication;

import com.techsolutio.products.domain.ApplicationUser;

import java.time.LocalDateTime;

public class ApplicationUserDTO {

    private Long id;
    private String name;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private ApplicationUserDTO() { }

    private ApplicationUserDTO(Long id, String name, String username, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.createAt = createAt;
        this.updateAt = updateAt;
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
}
