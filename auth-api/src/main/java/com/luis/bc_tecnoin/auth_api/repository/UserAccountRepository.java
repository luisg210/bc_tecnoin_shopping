package com.luis.bc_tecnoin.auth_api.repository;

import com.luis.bc_tecnoin.auth_api.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}