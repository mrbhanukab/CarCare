package sliit.pg09.carcare.pendingAppointment;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.VehicleRepository;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.List;

@Service
public class PendingAppointmentService {
    private final PendingAppointmentRepository pendingAppointmentRepository;
    private final VehicleService vehicleService;

    public PendingAppointmentService(PendingAppointmentRepository pendingAppointmentRepository, VehicleRepository vehicleRepository, VehicleService vehicleService) {
        this.pendingAppointmentRepository = pendingAppointmentRepository;
        this.vehicleService = vehicleService;
    }

    public List<PendingAppointment> getPendingAppointments(String vehicle) {
        return pendingAppointmentRepository.findByVehicle(vehicleService.getVehicleByLicense(vehicle));
    }
}
