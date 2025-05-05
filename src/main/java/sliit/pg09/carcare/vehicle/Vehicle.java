package sliit.pg09.carcare.vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.vehicle.model.Model;

@Data
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    private String license;
    private String vin;
    private boolean removed;

    @ManyToOne
    @JoinColumn(name = "number")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "email")
    private Client client;

    public Vehicle(String license, String vin, Model model, Client client) {
        this.license = license;
        this.vin = vin;
        this.model = model;
        this.client = client;
    }
}