package sliit.pg09.carcare.admin;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
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

    @HxRequest
    @GetMapping("/screen/emergencies")
    public String getEmergencyScreen(Model model) {
        return adminService.verifyUserStatus("Admin/Screens/EmergenciesDashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/appointments-requests")
    public String getAppointmentsRequests(Model model) {
        return adminService.verifyUserStatus("Admin/Screens/AppointmentsDashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/ongoing-appointments")
    public String getOngoingAppointmentsRequests(Model model) {
        return adminService.verifyUserStatus("Admin/Screens/OngoingDashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/completed-appointments")
    public String getCompletedAppointmentsRequests(Model model) {
        return adminService.verifyUserStatus("Admin/Screens/CompletedDashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/log")
    public String getLogs(Model model) {
        return adminService.verifyUserStatus("Admin/Screens/LogsDashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/clients")
    public String getClients(Model model) {
        return adminService.verifyUserStatus("Admin/Screens/Clients", model);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "Admin/Login";
    }

}
