package sliit.pg09.carcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sliit.pg09.carcare.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
