package sliit.pg09.carcare.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final AdminService adminService;
    private final AdminRepository adminRepository;

    public AdminInitializer(AdminService adminService, AdminRepository adminRepository) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

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