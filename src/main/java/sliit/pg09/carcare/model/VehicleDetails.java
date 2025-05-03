package sliit.pg09.carcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class VehicleDetails {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int VIN;
    private String vehicleType;
    private String vehicleModel;
    private String vehicleColor;
    private String vehicleLicensePlate;
    private String vehicleYear;
    private String vehicleEngineNumber;
    private String vehicleEngineType;
    private String vehcleNumber;
    private String seatingCapacity;
    private String noOfSeats;
    private String noOfDoors;
    private String fuelType;
    private String transmission;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}