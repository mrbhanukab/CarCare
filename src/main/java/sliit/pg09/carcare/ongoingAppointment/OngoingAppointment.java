package sliit.pg09.carcare.ongoingAppointment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class OngoingAppointment {
    @EmbeddedId
    private OngoingAppointmentId id;

    @ManyToOne
    @MapsId("license")
    @JoinColumn(name = "vehicle_license")
    private Vehicle vehicle;

    // Convenience method to set both vehicle and appointment time
    public void setAppointmentDetails(Vehicle vehicle, LocalDateTime appointmentTime) {
        if (this.id == null) {
            this.id = new OngoingAppointmentId();
        }
        this.vehicle = vehicle;
        this.id.setLicense(vehicle.getLicense());
        this.id.setAppointmentTime(appointmentTime);
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class OngoingAppointmentId implements Serializable {
        private String license;  // Matches Vehicle's @Id field name
        private LocalDateTime appointmentTime;
    }
}