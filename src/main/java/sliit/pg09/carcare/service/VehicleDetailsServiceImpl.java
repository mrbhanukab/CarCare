package sliit.pg09.carcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sliit.pg09.carcare.model.VehicleDetails;
import sliit.pg09.carcare.repository.VehicleDetailsRespository;

@Service
public class VehicleDetailsServiceImpl {
    @Autowired
    VehicleDetailsRespository vehicleDetailsRespository;

    public void add(VehicleDetails vehicleDetails) {
        vehicleDetailsRespository.save(vehicleDetails);
    }


}