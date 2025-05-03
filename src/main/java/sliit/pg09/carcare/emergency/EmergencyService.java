package sliit.pg09.carcare.emergency;

import org.springframework.stereotype.Service;

@Service
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;

    public EmergencyService(EmergencyRepository emergencyRepository) {
        this.emergencyRepository = emergencyRepository;
    }
}
