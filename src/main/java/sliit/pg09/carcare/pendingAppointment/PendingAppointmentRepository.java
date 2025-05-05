package sliit.pg09.carcare.pendingAppointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.List;

@Repository
public interface PendingAppointmentRepository extends JpaRepository<PendingAppointment, PendingAppointment.PendingAppointmentId> {
    List<PendingAppointment> findByVehicle(Vehicle vehicle);
}
