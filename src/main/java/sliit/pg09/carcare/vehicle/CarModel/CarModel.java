package sliit.pg09.carcare.vehicle.CarModel;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class CarModel {

    @Id
    private String number;
    @Enumerated(EnumType.STRING)
    private color color;
    private int year;
    private String image;
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    public CarModel(String number, color color, int year) {
        this.number = number;
        this.color = color;
        this.year = year;
    }

    public enum color {
        RED, BLUE, GREEN, YELLOW, BLACK, WHITE
    }
}