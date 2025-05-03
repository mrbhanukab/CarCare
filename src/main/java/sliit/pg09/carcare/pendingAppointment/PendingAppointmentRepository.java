package sliit.pg09.carcare.pendingAppointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingAppointmentRepository extends JpaRepository<PendingAppointment, PendingAppointment.PendingAppointmentId> {
}
