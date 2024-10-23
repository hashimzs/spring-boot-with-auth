package com.ga.basic_auth.service;

import com.ga.basic_auth.dto.request.ResetPasswordRequestDto;
import com.ga.basic_auth.dto.response.ResponseTemplate;
import com.ga.basic_auth.model.PasswordResetToken;
import com.ga.basic_auth.model.User;
import com.ga.basic_auth.repository.IResetTokenRepository;
import com.ga.basic_auth.repository.IUserRepository;
import com.ga.basic_auth.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetTokenService {

    @Autowired
    private IResetTokenRepository resetTokenRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    @Lazy
    private EmailUtil emailUtil;

    @Autowired
    @Lazy
    public PasswordEncoder passwordEncoder;


    public ResponseEntity<?> generateResetToken(String email){
        Optional<User> user=this.userRepository.findByEmail(email);

        if(user.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no Registered user with the same email has been found");

        PasswordResetToken resetToken= new PasswordResetToken(0, UUID.randomUUID().toString(),new Date(),user.get());

        StringBuilder body = new StringBuilder();
        body.append("Please click on the following link to resetPassword your account:\n\n");
        body.append("http://localhost:5000/ResetPassword.html?token=" + resetToken.getToken() + "\n\n");
        emailUtil.sendEmail(email, "reset password", body.toString());

        this.resetTokenRepository.save(resetToken);

        return new ResponseTemplate(HttpStatus.OK,"email has been sent");
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequestDto requestDto){
        Optional<PasswordResetToken> resetToken=this.resetTokenRepository.findByToken(requestDto.getToken());

        if(resetToken.isEmpty() || tokenHasExpired(resetToken.get()))
            return ResponseEntity.badRequest().body("Reset password link has expired");

        User user=resetToken.get().getUser();

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        this.userRepository.save(user);

        this.resetTokenRepository.deleteById(resetToken.get().getId());

        return new ResponseTemplate(HttpStatus.OK,"Password has been changed");
    }

    private static boolean tokenHasExpired(PasswordResetToken passwordResetToken){
        Instant then = passwordResetToken.getIssuedAt().toInstant();
        Duration threshold = Duration.ofMinutes(10);
        return Duration.between(then, Instant.now()).toSeconds() > threshold.toSeconds();
    }
}
