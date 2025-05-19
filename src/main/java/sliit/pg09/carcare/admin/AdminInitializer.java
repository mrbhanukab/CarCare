package sliit.pg09.carcare.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    
    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            adminService.createAdmin(
                    "admin@luxauto.com",
                    "Admin",
                    "admin123"
            );
        }
        System.out.println("Admin created");
    }
}