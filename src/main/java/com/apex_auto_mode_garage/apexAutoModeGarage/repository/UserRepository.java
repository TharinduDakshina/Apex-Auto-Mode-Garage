package com.apex_auto_mode_garage.apexAutoModeGarage.repository;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    UserEntity findByUserName(String userName);
}
