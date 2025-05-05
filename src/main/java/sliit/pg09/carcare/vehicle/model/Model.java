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
    private color color;
    private type type;
    private int year;
    private int noOfCylinders;
    private int power;
    private double length;
    private double width;
    private double wheelBase;
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    public Model(String number, color color, type type, int year, int noOfCylinders, int power, double length, double width, double wheelBase) {
        this.number = number;
        this.color = color;
        this.type = type;
        this.year = year;
        this.noOfCylinders = noOfCylinders;
        this.power = power;
        this.length = length;
        this.width = width;
        this.wheelBase = wheelBase;
    }

    public enum color {
        RED, BLUE, GREEN, YELLOW, BLACK, WHITE
    }

    public enum type {
        SEDAN, SUV, TRUCK, COUPE, HATCHBACK
    }
}