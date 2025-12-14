package com.apex_auto_mode_garage.apexAutoModeGarage.service;

import com.apex_auto_mode_garage.apexAutoModeGarage.Entity.VehicleEntity;
import com.apex_auto_mode_garage.apexAutoModeGarage.model.VehicleModel;
import com.apex_auto_mode_garage.apexAutoModeGarage.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    public VehicleService(VehicleRepository vehicleRepository, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.modelMapper = modelMapper;
    }

    public List<VehicleModel> fetchAllVehicles() throws SQLException {
        try {
            List<VehicleEntity> all = vehicleRepository.findAll();
            return convertEntityToModel(all);
        }catch (Exception e){
            throw new SQLException(e);
        }
    }

    private List<VehicleModel> convertEntityToModel(List<VehicleEntity> allVehicles){
        return allVehicles.stream()
                .map(entity -> modelMapper.map(entity, VehicleModel.class))
                .collect(Collectors.toList());
    }
}
