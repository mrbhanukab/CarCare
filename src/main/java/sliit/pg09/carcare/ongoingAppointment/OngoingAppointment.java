package sliit.pg09.carcare.ongoingAppointment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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

    // Store services as JSON
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> serviceNames;

    public void setAppointmentDetails(Vehicle vehicle, LocalDateTime appointmentTime, Set<String> services) {
        if (this.id == null) {
            this.id = new OngoingAppointmentId();
        }
        this.vehicle = vehicle;
        this.id.setLicense(vehicle.getLicense());
        this.id.setAppointmentTime(appointmentTime);
        this.serviceNames = services;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class OngoingAppointmentId implements Serializable {
        private String license;
        private LocalDateTime appointmentTime;
    }
}