package com.apex_auto_mode_garage.apexAutoModeGarage.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String vehicle_name;
    @Column(nullable = false)
    private int engine_capacity;
    @Column(nullable = false)
    private int cylinder_capacity;
    @Column(nullable = false)
    private int brake_horsepower;
    @Column(nullable = false)
    private double total_runs;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String image_url;
    @Column(nullable = false)
    private String vehicle_condition;
}
