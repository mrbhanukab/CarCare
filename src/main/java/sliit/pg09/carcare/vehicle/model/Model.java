package sliit.pg09.carcare.vehicle.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Model {
    @Id
    private String number;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;
}