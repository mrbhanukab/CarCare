package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngoingAppointmentRepository extends JpaRepository<OngoingAppointment, OngoingAppointment.OngoingAppointmentId> {
}
