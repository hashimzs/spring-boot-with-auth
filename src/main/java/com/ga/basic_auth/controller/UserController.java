package com.ga.basic_auth.controller;

import com.ga.basic_auth.dto.response.UserDto;
import com.ga.basic_auth.model.ImageDetails;
import com.ga.basic_auth.model.User;
import com.ga.basic_auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
        this.userService.deleteUserById(id);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto){
        return this.userService.updateUser(userDto);
    }

    @PostMapping
    public UserDto saveUser(@RequestBody User user){
        return UserDto.toUserDto(this.userService.saveUser(user));
    }

    @PostMapping("/image")
    public ImageDetails saveImage(@RequestParam("file") MultipartFile file) throws IOException {
        return this.userService.saveImage(file);
    }

    @DeleteMapping("/v2/image")
    public void deleteImage() throws IOException {
        this.userService.deleteUserImage();
    }

}
