package sliit.pg09.carcare.vehicle.model;

import org.springframework.stereotype.Service;

@Service
public class ModelService {
    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {

        this.modelRepository = modelRepository;
    }

    public Model getModelByNumber(String number) {
        return modelRepository.findById(number).orElse(null);
    }

    public Model updateModel(Model model) {
        return modelRepository.save(model);
    }
}
