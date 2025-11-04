package com.apex_auto_mode_garage.apexAutoModeGarage.service;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.UserEntity;
import com.apex_auto_mode_garage.apexAutoModeGarage.repository.UserRepository;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity register(UserEntity user){
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            return userRepository.save(user);
        }catch (SqlScriptException exception){
            throw new RuntimeException(exception.getLocalizedMessage());
        }
    }
}
