package sliit.pg09.carcare.vehicle.model;

import org.springframework.stereotype.Service;

@Service
public class ModelService {
    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }
}
