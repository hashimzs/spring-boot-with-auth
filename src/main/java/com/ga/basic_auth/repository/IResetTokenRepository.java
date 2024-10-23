package com.ga.basic_auth.repository;

import com.ga.basic_auth.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    public Optional<PasswordResetToken> findByToken(String token);
}
