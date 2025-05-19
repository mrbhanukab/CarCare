package sliit.pg09.carcare.ongoingAppointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.pg09.carcare.ongoingAppointment.OngoingAppointment.OngoingAppointmentId;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
    
@Service
@RequiredArgsConstructor
public class OngoingAppointmentService {
    private final OngoingAppointmentRepository ongoingAppointmentRepository;
    private final VehicleService vehicleService;

    public void createOngoingAppointment(String license, LocalDateTime appointmentDate, Set<String> services) {
        OngoingAppointment ongoingAppointment = new OngoingAppointment();
        ongoingAppointment.setAppointmentDetails(vehicleService.getVehicleByLicense(license), appointmentDate, services);
        ongoingAppointmentRepository.save(ongoingAppointment);
    }

    public OngoingAppointment getAppointmentById(String vehicleLicense, LocalDateTime appointmentTime) {
        OngoingAppointmentId appointmentId = new OngoingAppointment.OngoingAppointmentId();
        appointmentId.setLicense(vehicleLicense);
        appointmentId.setAppointmentTime(appointmentTime);
        return ongoingAppointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }

    public List<OngoingAppointment> getAllOngoingAppointments() {
        return ongoingAppointmentRepository.findAll();
    }

    public void deleteOngoingAppointment(String license, LocalDateTime appointmentTime) {
        OngoingAppointment ongoingAppointment = getAppointmentById(license, appointmentTime);
        ongoingAppointmentRepository.delete(ongoingAppointment);
    }
}