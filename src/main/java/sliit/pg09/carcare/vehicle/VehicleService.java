package sliit.pg09.carcare.vehicle;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.client.ClientRepository;
import sliit.pg09.carcare.vehicle.model.CarModel;
import sliit.pg09.carcare.vehicle.model.ModelRepository;
import java.util.List;

@Service
public class VehicleService
{
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final ModelRepository modelRepository;

    public VehicleService(VehicleRepository vehicleRepository, ClientRepository clientRepository, ModelRepository modelRepository) {
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
        this.modelRepository = modelRepository;
    }


    public void updateVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public boolean createVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getLicense() == null || vehicle.getModel() == null) {
            return false;
        }

        if (vehicleRepository.existsById(vehicle.getLicense())) {
            return false;
        }

        // Fetch the complete model entity
        CarModel model = modelRepository.findById(vehicle.getModel().getNumber()).orElse(null);
        if (model == null) {
            return false;
        }

        vehicle.setModel(model);
        vehicle.setRemoved(false);
        vehicleRepository.save(vehicle);
        return true;
    }

    public Vehicle getVehicleByLicense(String license) {
        return vehicleRepository.findById(license).orElse(null);
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}