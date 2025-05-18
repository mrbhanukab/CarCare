package sliit.pg09.carcare.nextService;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class NextService {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Vehicle vehicle;

    private int currentMileage;
    private int nextServiceMileage;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<ServiceType> serviceTypes;
}