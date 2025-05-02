package sliit.pg09.carcare.old.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.old.repository.VehicleDetailsRepository;

@Service
public class VehicleDetailsService {
    private final VehicleDetailsRepository vehicleDetailsRepository;

    public VehicleDetailsService(VehicleDetailsRepository vehicleDetailsRepository) {
        this.vehicleDetailsRepository = vehicleDetailsRepository;
    }


}