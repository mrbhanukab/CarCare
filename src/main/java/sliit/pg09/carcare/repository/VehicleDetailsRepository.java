package sliit.pg09.carcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sliit.pg09.carcare.model.VehicleDetails;

public interface VehicleDetailsRepository extends JpaRepository<VehicleDetails, Integer> {
}
