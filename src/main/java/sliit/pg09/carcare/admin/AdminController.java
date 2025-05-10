package sliit.pg09.carcare.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @RequestMapping(value = {"", "/"})
    public String getDashboard(Model model) {
        return adminService.verifyUserStatus("Admin/Dashboard", model);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "Admin/Login";
    }

}
