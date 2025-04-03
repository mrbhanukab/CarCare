package sliit.pg09.carcare.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.repository.VehicleDetailsRespository;

@Service
public class VehicleDetailsService {
    private final VehicleDetailsRespository vehicleDetailsRespository;

    public VehicleDetailsService(VehicleDetailsRespository vehicleDetailsRespository) {
        this.vehicleDetailsRespository = vehicleDetailsRespository;
    }


}