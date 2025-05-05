package sliit.pg09.carcare.client;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Client {

    @Id
    private String email;
    private String name;
    private String phone;
    private String address;
    private String nic;
    private String imageUrl;

    @CreationTimestamp
    private LocalDate created;
    @UpdateTimestamp
    private LocalDate updated;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    public Client(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}