package sliit.pg09.carcare.emergency;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.Vehicle;

import java.time.LocalDateTime;

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

    }

}
