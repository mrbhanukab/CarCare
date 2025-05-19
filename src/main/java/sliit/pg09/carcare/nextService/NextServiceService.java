package sliit.pg09.carcare.nextService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NextServiceService {
    private final NextServiceRepository nextServiceRepository;

    public Optional<NextService> getNextServiceByLicense(String license) {
        System.out.println("Fetching next service for vehicle with license: " + license);
        return nextServiceRepository.findById(license);
    }

    public void saveNextService(NextService nextService) {
        nextServiceRepository.save(nextService);
    }
}