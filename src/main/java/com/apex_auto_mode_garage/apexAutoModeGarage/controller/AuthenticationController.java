package com.apex_auto_mode_garage.apexAutoModeGarage.controller;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.UserEntity;
import com.apex_auto_mode_garage.apexAutoModeGarage.model.UserModel;
import com.apex_auto_mode_garage.apexAutoModeGarage.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody UserModel user) {
        user.setRole("USER");
        System.out.println(user.toString());
        try {
            UserEntity userEntity = convertUserModelToUserEntity(user);
            UserEntity registeredUser = userService.register(userEntity);
            return ResponseEntity.ok(registeredUser);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body("User registration process is failed.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody UserModel userCredentials){
        try {
            String token = userService.verifyUser(convertUserModelToUserEntity(userCredentials));
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getLocalizedMessage());
        }
    }

    private UserEntity convertUserModelToUserEntity(UserModel userModel) throws IOException {
        return modelMapper.map(userModel, UserEntity.class);
    }
}
