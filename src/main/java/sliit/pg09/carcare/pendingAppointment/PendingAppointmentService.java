package sliit.pg09.carcare.pendingAppointment;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.VehicleRepository;

import java.util.List;

@Service
public class PendingAppointmentService {
    private final PendingAppointmentRepository pendingAppointmentRepository;
    private final ve;

    public PendingAppointmentService(PendingAppointmentRepository pendingAppointmentRepository, VehicleRepository vehicleRepository) {
        this.pendingAppointmentRepository = pendingAppointmentRepository;
    }

    public List<PendingAppointment> getPendingAppointments(String vehicle) {
        return pendingAppointmentRepository.findByVehicle(vehicleRepository.)
    }
}
