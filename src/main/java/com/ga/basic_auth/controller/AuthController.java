package com.ga.basic_auth.controller;

import com.ga.basic_auth.dto.request.ChangePasswordRequestDto;
import com.ga.basic_auth.dto.request.ForgotPasswordRequestDto;
import com.ga.basic_auth.dto.request.LoginRequestDto;
import com.ga.basic_auth.dto.request.ResetPasswordRequestDto;
import com.ga.basic_auth.dto.response.UserDto;
import com.ga.basic_auth.model.User;
import com.ga.basic_auth.service.ResetTokenService;
import com.ga.basic_auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/users")
public class AuthController {
    @Autowired
    public UserService userService;

    @Autowired
    public ResetTokenService resetTokenService;

    //@GetMapping()
    //public UserDto getLoggedUserInfo(){
   //     return this.userService.getLoggedUser();
    //}


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user){
        return userService.registerUser(user);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam(value = "token")String token){
        return userService.verifyEmail(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        return this.userService.login(loginRequestDto);
    }

    @PostMapping("/changePassword")
    public  ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto){
        return this.userService.changePassword(changePasswordRequestDto);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){
        return this.resetTokenService.generateResetToken(forgotPasswordRequestDto.getEmail());
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto requestDto){
        return this.resetTokenService.resetPassword(requestDto);
    }

    @PutMapping()
    public UserDto updateUserProfile(@RequestBody UserDto user){
        return this.userService.updateUser(user);
    }

}
