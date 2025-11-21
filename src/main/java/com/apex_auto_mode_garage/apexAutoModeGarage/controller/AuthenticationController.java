package com.apex_auto_mode_garage.apexAutoModeGarage.controller;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.UserEntity;
import com.apex_auto_mode_garage.apexAutoModeGarage.model.UserModel;
import com.apex_auto_mode_garage.apexAutoModeGarage.model.dto.VerifyUserDto;
import com.apex_auto_mode_garage.apexAutoModeGarage.service.JWTService;
import com.apex_auto_mode_garage.apexAutoModeGarage.service.UserService;
import io.jsonwebtoken.Claims;
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
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    @Autowired
    public AuthenticationController(UserService userService, ModelMapper modelMapper, JWTService jwtService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

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
    public ResponseEntity<Object> userLogin(@RequestBody UserModel userCredentials){
        try {
            VerifyUserDto verifyUserDto = userService.verifyUser(convertUserModelToUserEntity(userCredentials));
            return ResponseEntity.status(HttpStatus.OK).body(verifyUserDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/accessToken")
    public ResponseEntity<String> getAccessTokenByRefreshToken(@RequestHeader String authorization){
        if (!authorization.isEmpty()) {
            String refreshToken = authorization.replace("Bearer ", "");
            String newAccessToken = jwtService.getNewAccessToken(refreshToken);
            return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
        }else {
            System.out.println("refresh token invalided or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token invalided.");
        }

    }

    private UserEntity convertUserModelToUserEntity(UserModel userModel) throws IOException {
        return modelMapper.map(userModel, UserEntity.class);
    }
}
