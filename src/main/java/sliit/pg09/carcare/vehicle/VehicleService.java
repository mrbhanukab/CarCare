package sliit.pg09.carcare.vehicle;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.client.ClientRepository;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;

    public VehicleService(VehicleRepository vehicleRepository, ClientRepository clientRepository) {

        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
    }

    public void updateVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public boolean createVehicle (Vehicle vehicle) {
        if(vehicle == null || vehicle.getLicense() == null) {
            return false;
        }

        if(vehicleRepository.existsById(vehicle.getLicense())) {
            return false;
        }

        vehicleRepository.save(vehicle);
        return true;
    }

    public Vehicle getVehicleByLicense(String license) {
        return vehicleRepository.findById(license).orElse(null);
    }

}
