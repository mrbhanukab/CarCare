package sliit.pg09.carcare.old.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String VIN;
    private String vehicleModel;

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private VehicleDetails vehicleDetails;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}