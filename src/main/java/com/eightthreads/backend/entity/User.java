package com.eightthreads.backend.entity;

import com.eightthreads.backend.common.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 20)
    private Role role;

    @Column(length = 20)
    private String status = "active";

    @Column(name = "auth_provider", length = 20)
    private String authProvider = "local";

    @Column(name = "provider_id")
    private String providerId;

    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
