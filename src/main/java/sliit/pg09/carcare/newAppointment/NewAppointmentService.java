package sliit.pg09.carcare.newAppointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewAppointmentService {
    private final NewAppointmentRepository newAppointmentRepository;
    private final VehicleService vehicleService;

    public List<NewAppointment> getNewAppointmentsByVehicle(String license) {
        return newAppointmentRepository.findByVehicle(vehicleService.getVehicleByLicense(license));
    }

    public void saveAppointment(NewAppointment newAppointment) {
        newAppointmentRepository.save(newAppointment);
    }

    public List<NewAppointment> getNewAppointments() {
        return newAppointmentRepository.findAll();
    }

    public void deleteAppointmentById(NewAppointment.PendingAppointmentId id) {
        newAppointmentRepository.deleteById(id);
    }
}