package sliit.pg09.carcare.emergency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;
    private final VehicleService vehicleService;

    public boolean vehicleHasEmergency(String vehicleLicense) {
        return emergencyRepository.findEmergencyByVehicle_License(vehicleLicense).isHandled();
    }

    public void createEmergency(String vehicleLicence, Double latitude, Double longitude, LocalDateTime timestamp) {
        Vehicle vehicle = vehicleService.getVehicleByLicense(vehicleLicence);
        Emergency.Location emergencyLocation = new Emergency.Location(latitude, longitude);
        Emergency emergency = new Emergency(vehicle, timestamp, emergencyLocation);
        emergency.setHandled(false);  // Default to unhandled
        emergencyRepository.save(emergency);
    }

    public void createEmergency(Vehicle vehicle, LocalDateTime timestamp, Double latitude, Double longitude) {
        emergencyRepository.save(new Emergency(vehicle, timestamp, new Emergency.Location(latitude, longitude)));
    }

    public boolean markRequestAsHandled(String vehicleLicense, LocalDateTime emergencyTime) {
        Emergency.EmergencyId id = new Emergency.EmergencyId();
        id.setVehicleLicense(vehicleLicense);
        id.setEmergencyTime(emergencyTime);

        Optional<Emergency> optionalEmergency = emergencyRepository.findById(id);
        if (optionalEmergency.isPresent()) {
            Emergency emergency = optionalEmergency.get();
            emergency.setHandled(true);
            emergencyRepository.save(emergency);
            return true;
        }
        return false;
    }

    public List<Emergency> getActiveEmergencies() {
        return emergencyRepository.findAll().stream()
                .filter(emergency -> !emergency.isHandled())
                .toList();
    }
}