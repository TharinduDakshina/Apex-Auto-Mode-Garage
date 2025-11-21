package com.apex_auto_mode_garage.apexAutoModeGarage.repository;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity,String> {
}
