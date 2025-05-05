package sliit.pg09.carcare.emergency;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Emergency {
    @EmbeddedId
    private EmergencyId id;
    private Location location;
    private boolean handled;

    @MapsId("vehicleLicense")
    @ManyToOne
    @JoinColumn(name = "vehicle")
    private Vehicle vehicle;

    public Emergency(Vehicle vehicle, LocalDateTime time, Location location) {
        this.id = new EmergencyId();
        this.vehicle = vehicle;
        this.id.setVehicleLicense(vehicle.getLicense());
        this.id.setEmergencyTime(time);
        this.location = location;
    }

    @Data
    @NoArgsConstructor
    @Embeddable
    public static class Location {
        private Double latitude;
        private Double longitude;

        public Location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class EmergencyId implements java.io.Serializable {
        @Column(name = "vehicle")
        private String vehicleLicense;

        @Column(name = "time")
        private LocalDateTime emergencyTime;
    }
}