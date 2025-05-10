package sliit.pg09.carcare.vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.vehicle.model.CarModel;

@Data
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    private String license;
    private boolean removed;

    @ManyToOne
    @JoinColumn(name = "number")
    private CarModel model;

    @ManyToOne
    @JoinColumn(name = "email")
    private Client client;

    public Vehicle(String license, CarModel model, Client client) {
        this.license = license;
        this.model = model;
        this.client = client;
    }
}