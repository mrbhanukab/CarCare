package sliit.pg09.carcare.emergency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Emergency.EmergencyId> {
    Emergency findEmergencyByVehicle_License(String vehicleLicense);
}
