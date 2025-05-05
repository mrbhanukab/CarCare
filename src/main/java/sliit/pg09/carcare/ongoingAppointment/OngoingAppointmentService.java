package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.List;

@Service
public class OngoingAppointmentService {
    private final OngoingAppointmentRepository ongoingAppointmentRepository;
    private final VehicleService vehicleService;

    public OngoingAppointmentService(OngoingAppointmentRepository ongoingAppointmentRepository, VehicleService vehicleService) {
        this.ongoingAppointmentRepository = ongoingAppointmentRepository;
        this.vehicleService = vehicleService;
    }

    public List<OngoingAppointment> getOngoingAppointments(String vehicle) {
        return ongoingAppointmentRepository.findByVehicle(vehicleService.getVehicleByLicense(vehicle));
    }
}
