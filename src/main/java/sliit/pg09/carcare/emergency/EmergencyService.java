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

    public void createEmergency(String location, String vehicleLicence) {
        Vehicle vehicle = vehicleService.findVehicle(vehicleLicence);

        Emergency emergency = new Emergency(vehicle, LocalDateTime.now());
        emergency.setLocation(location);

        emergencyRepository.save(emergency);
    }
}