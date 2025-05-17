package sliit.pg09.carcare.vehicle;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.client.ClientService;
import sliit.pg09.carcare.vehicle.CarModel.CarModel;
import sliit.pg09.carcare.vehicle.CarModel.CarModelService;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CarModelService carModelService;
    private final ClientService clientService;

    public VehicleService(VehicleRepository vehicleRepository, CarModelService carModelService, ClientService clientService) {
        this.vehicleRepository = vehicleRepository;
        this.carModelService = carModelService;
        this.clientService = clientService;
    }


    public void updateVehicle(String license, String modelNumber) {
        Vehicle vehicle = vehicleRepository.findById(license).orElse(null);
        if (vehicle != null) {
            CarModel model = carModelService.getModelByNumber(modelNumber);
            if (model != null) {
                vehicle.setModel(model);
                vehicleRepository.save(vehicle);
            }
        }
    }

    public Vehicle createVehicle(String license, String modelNumber) {
        if (license == null || license.isBlank())
            throw new IllegalArgumentException("Vehicle license cannot be null or empty");

        if (modelNumber == null || modelNumber.isBlank())
            throw new IllegalArgumentException("Model number cannot be null or empty");

        if (vehicleRepository.existsById(license))
            throw new IllegalStateException("Vehicle with license " + license + " already exists");

        CarModel carModel = carModelService.getModelByNumber(modelNumber);
        if (carModel == null)
            throw new IllegalArgumentException("Car model with number " + modelNumber + " not found");

        var clientOptional = clientService.getCurrentUser();
        if (clientOptional.isEmpty())
            throw new IllegalStateException("No authenticated client found");

        Vehicle vehicle = new Vehicle(license, carModel, clientOptional.get());
        vehicle.setRemoved(false);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleByLicense(String license) {
        return vehicleRepository.findById(license).orElse(null);
    }

    public List<Vehicle> getVehiclesByCurrentClient() {
        var clientOptional = clientService.getCurrentUser();
        if (clientOptional.isEmpty())
            throw new IllegalStateException("No authenticated client found");

        return vehicleRepository.getVehiclesByClient(clientOptional.get())
                .stream()
                .filter(vehicle -> !vehicle.isRemoved())
                .toList();
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}