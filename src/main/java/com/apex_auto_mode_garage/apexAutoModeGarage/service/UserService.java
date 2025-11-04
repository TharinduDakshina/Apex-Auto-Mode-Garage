package com.apex_auto_mode_garage.apexAutoModeGarage.service;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.UserEntity;
import com.apex_auto_mode_garage.apexAutoModeGarage.repository.UserRepository;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authManager;

    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.jwtService =  jwtService;
    }

    public UserEntity register(UserEntity user){
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            return userRepository.save(user);
        }catch (SqlScriptException exception){
            throw new RuntimeException(exception.getLocalizedMessage());
        }
    }

    public String verifyUser(UserEntity user) throws Exception {
        Authentication authenticate = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
        );

        if (authenticate.isAuthenticated()){
            UserEntity user1 = userRepository.findByUserName(user.getUserName());
            Object principal = authenticate.getPrincipal();
            return jwtService.getJWTToken(user1.getUserName(), user1.getRole(), user1.getId());
        }else {
            throw new Exception("Username or password not found !");
        }
    }
}
