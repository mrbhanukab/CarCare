package sliit.pg09.carcare.vehicle;

import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findVehicle(String vehicleId) {
        return vehicleRepository.findById(vehicleId).orElse(null);
    }
}
