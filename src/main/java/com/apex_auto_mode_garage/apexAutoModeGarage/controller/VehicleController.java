package com.apex_auto_mode_garage.apexAutoModeGarage.controller;

import com.apex_auto_mode_garage.apexAutoModeGarage.model.VehicleModel;
import com.apex_auto_mode_garage.apexAutoModeGarage.service.VehicleService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/allVehicles")
    public ResponseEntity<?> getAllVehicles() throws SQLException {
        List<VehicleModel> vehicles = vehicleService.fetchAllVehicles();
        return ResponseEntity.status(HttpStatus.OK).body(vehicles);
    }
}
