package sliit.pg09.carcare.pendingAppointment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class NewAppointment {
    @EmbeddedId
    private PendingAppointmentId id;

    @ManyToOne
    @MapsId("license")
    private Vehicle vehicle;

    // Store services as JSON
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> serviceNames;

    // Store dates as JSON
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<LocalDateTime> preferredDateTimes;

    private String notes;

    // Method to get services as ServiceType set
    public Set<ServiceType> getServices() {
        if (serviceNames == null || serviceNames.isEmpty()) {
            return Set.of();
        }
        return serviceNames.stream()
                .map(ServiceType::valueOf)
                .collect(java.util.stream.Collectors.toSet());
    }

    // Method to set services from enum set
    public void setServices(Set<ServiceType> serviceTypes) {
        if (serviceTypes != null) {
            this.serviceNames = serviceTypes.stream()
                    .map(Enum::name)
                    .collect(java.util.stream.Collectors.toSet());
        }
    }

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
        private String license;
        private LocalDateTime createdTime;
    }
}