package sliit.pg09.carcare.emergency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;

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
    }
}