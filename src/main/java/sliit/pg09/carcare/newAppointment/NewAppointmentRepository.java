package sliit.pg09.carcare.newAppointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.util.List;

@Repository
public interface NewAppointmentRepository extends JpaRepository<NewAppointment, NewAppointment.PendingAppointmentId> {
    List<NewAppointment> findByVehicle(Vehicle vehicle);
}
