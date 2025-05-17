package sliit.pg09.carcare.completedAppointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface completedAppointmentRepository extends JpaRepository<completedAppointment, completedAppointment.CompletedAppointmentId> {
}