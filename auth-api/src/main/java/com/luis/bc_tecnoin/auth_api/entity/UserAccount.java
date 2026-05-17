package com.luis.bc_tecnoin.auth_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    private String password;
    private String role;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @CreatedDate
    private LocalDateTime createdDate;
}
