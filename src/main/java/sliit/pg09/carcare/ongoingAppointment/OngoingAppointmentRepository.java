package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.List;


@Repository
public interface OngoingAppointmentRepository extends JpaRepository<OngoingAppointment, OngoingAppointment.OngoingAppointmentId> {
    List<OngoingAppointment> findByVehicle(Vehicle vehicle);

    List<OngoingAppointment> findByVehicle_License(String vehicleLicense);
}
