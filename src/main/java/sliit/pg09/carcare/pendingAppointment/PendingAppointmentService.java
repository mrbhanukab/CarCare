package sliit.pg09.carcare.pendingAppointment;

import org.springframework.stereotype.Service;

@Service
public class PendingAppointmentService {
    private final PendingAppointmentRepository pendingAppointmentRepository;

    public PendingAppointmentService(PendingAppointmentRepository pendingAppointmentRepository) {
        this.pendingAppointmentRepository = pendingAppointmentRepository;
    }
}
