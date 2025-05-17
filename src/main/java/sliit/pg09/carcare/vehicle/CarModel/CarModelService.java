package sliit.pg09.carcare.vehicle.CarModel;

import org.springframework.stereotype.Service;

@Service
public class CarModelService {
    private final CarModelRepository carModelRepository;

    public CarModelService(CarModelRepository carModelRepository) {

        this.carModelRepository = carModelRepository;
    }

    public CarModel getModelByNumber(String number) {
        return carModelRepository.findById(number).orElse(null);
    }

    public CarModel updateModel(CarModel model) {
        return carModelRepository.save(model);
    }
}
