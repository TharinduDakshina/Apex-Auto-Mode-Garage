package com.apex_auto_mode_garage.apexAutoModeGarage.controller;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.UserEntity;
import com.apex_auto_mode_garage.apexAutoModeGarage.model.UserModel;
import com.apex_auto_mode_garage.apexAutoModeGarage.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    @Autowired
    private  UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody UserModel user) {
        System.out.println("======================================================");
        try {
            UserEntity userEntity = convertUserModelToUserEntity(user);
            UserEntity registeredUser = userService.register(userEntity);
            return ResponseEntity.ok(registeredUser);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body("User registration process is failed.");
        }
    }

    private UserEntity convertUserModelToUserEntity(UserModel userModel) throws IOException {
        UserEntity userEntity = new UserEntity();
        // Map fields from userModel to userEntity
        userEntity.setUserName(userModel.getUserName());
        userEntity.setEmail(userModel.getEmail());
        userEntity.setPassword(userModel.getPassword());
        // ... map other fields
        return userEntity;
    }
}
