package sliit.pg09.carcare.nextService;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.Optional;

public interface NextServiceRepository extends JpaRepository<NextService, String> {
    Optional<NextService> findByVehicle(Vehicle vehicle);
}