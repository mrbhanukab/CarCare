package sliit.pg09.carcare.vehicle;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.client.ClientService;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.nextService.NextService;
import sliit.pg09.carcare.nextService.NextServiceService;
import sliit.pg09.carcare.vehicle.CarModel.CarModel;
import sliit.pg09.carcare.vehicle.CarModel.CarModelService;

import java.util.List;
import java.util.Set;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CarModelService carModelService;
    private final ClientService clientService;
    private final NextServiceService nextServiceService;

    public VehicleService(VehicleRepository vehicleRepository, CarModelService carModelService, ClientService clientService, NextServiceService nextServiceService) {
        this.vehicleRepository = vehicleRepository;
        this.carModelService = carModelService;
        this.clientService = clientService;
        this.nextServiceService = nextServiceService;
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
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Create NextService record
        NextService nextService = new NextService();
        nextService.setVehicle(savedVehicle);
        nextService.setCurrentMileage(0);
        nextService.setNextServiceMileage(5000);
        nextService.setServiceTypes(Set.of(ServiceType.GENERAL_REPAIR));
        nextServiceService.saveNextService(nextService);

        return savedVehicle;
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

