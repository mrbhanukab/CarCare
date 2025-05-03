package sliit.pg09.carcare.emergency;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;

    public EmergencyService(EmergencyRepository emergencyRepository) {
        this.emergencyRepository = emergencyRepository;
    }

    public void createEmergency(String description, String location, String vehicleLicence) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicense(vehicleLicence);

        Emergency emergency = new Emergency(vehicle, LocalDateTime.now());

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
