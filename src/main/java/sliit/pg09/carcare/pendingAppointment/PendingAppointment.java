package sliit.pg09.carcare.pendingAppointment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class PendingAppointment {
    @EmbeddedId
    private PendingAppointmentId id;

    @ManyToOne
    @MapsId("license")  // Maps to the field name in PendingAppointmentId
    @JoinColumn(name = "vehicle_license")
    private Vehicle vehicle;

    private String description;
    private LocalDateTime scheduledTime;
    private String status;

    // Convenience method to set both vehicle and created time
    public void setAppointmentDetails(Vehicle vehicle, LocalDateTime createdTime) {
        if (this.id == null) {
            this.id = new PendingAppointmentId();
        }
        this.vehicle = vehicle;
        this.id.setLicense(vehicle.getLicense());
        this.id.setCreatedTime(createdTime);
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class PendingAppointmentId implements Serializable {
        private String license;  // Matches with Vehicle's @Id field name
        private LocalDateTime createdTime;
    }
}