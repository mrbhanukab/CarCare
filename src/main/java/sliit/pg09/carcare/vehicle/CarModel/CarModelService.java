package sliit.pg09.carcare.vehicle.CarModel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarModelService {
    private final CarModelRepository carModelRepository;

    public CarModel getModelByNumber(String number) {
        return carModelRepository.findById(number).orElse(null);
    }

    public CarModel updateModel(CarModel model) {
        return carModelRepository.save(model);
    }
}