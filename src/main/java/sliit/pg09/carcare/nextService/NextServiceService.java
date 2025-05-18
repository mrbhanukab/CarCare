package sliit.pg09.carcare.nextService;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NextServiceService {
    private final NextServiceRepository nextServiceRepository;

    public NextServiceService(NextServiceRepository nextServiceRepository) {
        this.nextServiceRepository = nextServiceRepository;
    }

    public Optional<NextService> getNextServiceByLicense(String license) {
        System.out.println("Fetching next service for vehicle with license: " + license);
        return nextServiceRepository.findById(license);
    }

    public void saveNextService(NextService nextService) {
        nextServiceRepository.save(nextService);
    }
}