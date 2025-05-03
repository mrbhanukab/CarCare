package sliit.pg09.carcare.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.repository.VehicleDetailsRepository;

@Service
public class VehicleDetailsService {
    private final VehicleDetailsRepository vehicleDetailsRepository;

    public VehicleDetailsService(VehicleDetailsRepository vehicleDetailsRepository) {
        this.vehicleDetailsRepository = vehicleDetailsRepository;
    }


}