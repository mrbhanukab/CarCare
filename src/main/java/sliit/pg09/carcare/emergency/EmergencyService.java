package sliit.pg09.carcare.emergency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;
    private final VehicleService vehicleService;

    @Autowired
    public EmergencyService(EmergencyRepository emergencyRepository, VehicleService vehicleService) {
        this.emergencyRepository = emergencyRepository;
        this.vehicleService = vehicleService;
    }

    public boolean vehicleHasEmergency(String vehicleLicense) {
        return emergencyRepository.findEmergencyByVehicle_License(vehicleLicense).isHandled();
    }

    public void createEmergency(String vehicleLicence, Double latitude, Double longitude, LocalDateTime timestamp) {

        Vehicle vehicle = vehicleService.findVehicle(vehicleLicence);
        Emergency.Location emergencyLocation = new Emergency.Location(latitude, longitude);
        Emergency emergency = new Emergency(vehicle, timestamp, emergencyLocation);
        emergencyRepository.save(emergency);
        emergency.setDescription(description);
        emergency.setLocation(location);
        emergency.setHandled(false);  // Default to unhandled

        emergencyRepository.save(emergency);
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

    List<Emergency> getOngoingEmergencies() {
        return emergencyRepository.findAll().stream()
                .filter(emergency -> !emergency.isHandled())
                .toList();
    }
}