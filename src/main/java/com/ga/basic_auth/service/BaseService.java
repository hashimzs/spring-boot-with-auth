package com.ga.basic_auth.service;

import com.ga.basic_auth.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public interface BaseService {
    default User getCurrUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
