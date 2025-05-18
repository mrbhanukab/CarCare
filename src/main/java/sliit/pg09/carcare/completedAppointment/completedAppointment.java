package sliit.pg09.carcare.completedAppointment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
public class completedAppointment {
    @EmbeddedId
    private CompletedAppointmentId id;

    @ManyToOne
    @MapsId("license")
    @JoinColumn(name = "vehicle_license")
    private Vehicle vehicle;

    // Store services as JSON
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> serviceNames;

    // Store billing information as JSON
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private BillingInfo billingInfo;

    private String notes;

    // Self-referencing associations for doubly linked list
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "prev_license", referencedColumnName = "vehicle_license"),
            @JoinColumn(name = "prev_completed_time", referencedColumnName = "completedTime")
    })
    private completedAppointment previousAppointment;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "next_license", referencedColumnName = "vehicle_license"),
            @JoinColumn(name = "next_completed_time", referencedColumnName = "completedTime")
    })
    private completedAppointment nextAppointment;

    // Method to get services as ServiceType set
    public Set<ServiceType> getServices() {
        if (serviceNames == null || serviceNames.isEmpty()) {
            return Set.of();
        }
        return serviceNames.stream()
                .map(ServiceType::valueOf)
                .collect(Collectors.toSet());
    }

    // Method to set services from enum set
    public void setServices(Set<ServiceType> serviceTypes) {
        if (serviceTypes != null) {
            this.serviceNames = serviceTypes.stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());
        }
    }

    public void setAppointmentDetails(Vehicle vehicle, LocalDateTime completedTime) {
        if (this.id == null) {
            this.id = new CompletedAppointmentId();
        }
        this.vehicle = vehicle;
        this.id.setLicense(vehicle.getLicense());
        this.id.setCompletedTime(completedTime);
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class CompletedAppointmentId implements Serializable {
        private String license;
        private LocalDateTime completedTime;
    }
}