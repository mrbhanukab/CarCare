package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.stereotype.Service;

@Service
public class OngoingAppointmentService {
    private final OngoingAppointmentRepository ongoingAppointmentRepository;

    public OngoingAppointmentService(OngoingAppointmentRepository ongoingAppointmentRepository) {
        this.ongoingAppointmentRepository = ongoingAppointmentRepository;
    }
}
