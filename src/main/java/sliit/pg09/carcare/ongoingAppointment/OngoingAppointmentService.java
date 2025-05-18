package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class OngoingAppointmentService {
    private final OngoingAppointmentRepository ongoingAppointmentRepository;
    private final VehicleService vehicleService;

    public OngoingAppointmentService(OngoingAppointmentRepository ongoingAppointmentRepository, VehicleService vehicleService) {
        this.ongoingAppointmentRepository = ongoingAppointmentRepository;
        this.vehicleService = vehicleService;
    }

    public void createOngoingAppointment(String license, LocalDateTime appointmentDate, Set<String> services) {
        OngoingAppointment ongoingAppointment = new OngoingAppointment();
        ongoingAppointment.setAppointmentDetails(vehicleService.getVehicleByLicense(license), appointmentDate, services);
        ongoingAppointmentRepository.save(ongoingAppointment);
    }
}