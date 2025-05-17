package sliit.pg09.carcare.completedAppointment;

import org.springframework.stereotype.Service;

@Service
public class completedAppointmentService {
    private final completedAppointmentRepository completedAppointmentRepository;

    public completedAppointmentService(completedAppointmentRepository completedAppointmentRepository) {
        this.completedAppointmentRepository = completedAppointmentRepository;
    }
}
