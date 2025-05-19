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
            adminService.createAdmin("admin1@porsche.com", "Admin1", "admin123");
            adminService.createAdmin("admin2@porsche.com", "Admin2", "admin123");
            adminService.createAdmin("admin3@porsche.com", "Admin3", "admin123");
            adminService.createAdmin("admin4@porsche.com", "Admin4", "admin123");
            adminService.createAdmin("admin5@porsche.com", "Admin5", "admin123");
        }
        System.out.println("Admin created");
    }
}