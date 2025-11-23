package com.apex_auto_mode_garage.apexAutoModeGarage.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleModel {
    private String id;
    private String vehicle_name;
    private int engine_capacity;
    private int cylinder_capacity;
    private int brake_horsepower;
    private double total_runs;
    private double price;
    private String image_url;
    private String vehicle_condition;
}
