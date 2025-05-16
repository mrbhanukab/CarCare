package sliit.pg09.carcare.pendingAppointment;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.VehicleRepository;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.List;

@Service
public class NewAppointmentService {
    private final NewAppointmentRepository newAppointmentRepository;
    private final VehicleService vehicleService;

    public NewAppointmentService(NewAppointmentRepository newAppointmentRepository, VehicleRepository vehicleRepository, VehicleService vehicleService) {
        this.newAppointmentRepository = newAppointmentRepository;
        this.vehicleService = vehicleService;
    }

    public List<NewAppointment> getPendingAppointments(String vehicle) {
        return newAppointmentRepository.findByVehicle(vehicleService.getVehicleByLicense(vehicle));
    }

    public void saveAppointment(NewAppointment newAppointment) {
        newAppointmentRepository.save(newAppointment);
    }
}
