package sliit.pg09.carcare.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.repository.VehicleDetailsRespository;

@Service
public class VehicleDetailsServiceImpl {
    private final VehicleDetailsRespository vehicleDetailsRespository;

    public VehicleDetailsServiceImpl(VehicleDetailsRespository vehicleDetailsRespository) {
        this.vehicleDetailsRespository = vehicleDetailsRespository;
    }


}