package com.ga.basic_auth.repository;

import com.ga.basic_auth.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserImageRepository extends JpaRepository<UserImage,Integer> {
    public Optional<UserImage> findByUserId(int userId);
}
