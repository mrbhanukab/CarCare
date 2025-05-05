package sliit.pg09.carcare.admin;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public String verifyUserStatus(String file, Model model) {
        return file;
    }
}
