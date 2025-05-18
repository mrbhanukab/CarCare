package sliit.pg09.carcare.admin;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sliit.pg09.carcare.client.ClientService;
import sliit.pg09.carcare.emergency.Emergency;
import sliit.pg09.carcare.emergency.EmergencyService;
import sliit.pg09.carcare.newAppointment.NewAppointment;
import sliit.pg09.carcare.newAppointment.NewAppointmentService;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    EmergencyService emergencyService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private NewAppointmentService newAppointmentService;

    @RequestMapping(value = {"", "/"})
    public String getDashboard(Model model) {
        List<Emergency> activeEmergencies = emergencyService.getActiveEmergencies();
        System.out.println("Active emergencies count: " + activeEmergencies.size());
        model.addAttribute("emergencies", activeEmergencies);
        model.addAttribute("activeCount", activeEmergencies.size());
        return adminService.verifyUserStatus("Admin/Dashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/emergencies")
    public String getEmergencyScreen(Model model) {
        List<Emergency> activeEmergencies = emergencyService.getActiveEmergencies();
        System.out.println("Active emergencies count: " + activeEmergencies.size());
        model.addAttribute("emergencies", activeEmergencies);
        model.addAttribute("activeCount", activeEmergencies.size());
        return adminService.verifyUserStatus("Admin/Screens/EmergenciesDashboard", model);
    }

    @HxRequest
    @GetMapping("/screen/appointments-requests")
    public String getAppointmentsRequests(Model model) {
        List<NewAppointment> newAppointments = newAppointmentService.getNewAppointments();
        model.addAttribute("appointments", newAppointments);
        model.addAttribute("activeCount", newAppointments.size());
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
    @GetMapping("/screen/clients")
    public String getClients(Model model) {
        model.addAttribute("clients", clientService.findAllClients());


        return adminService.verifyUserStatus("Admin/Screens/Clients", model);
    }

    @GetMapping("/screen/vehicles")
    public String getVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.findAll());
        return adminService.verifyUserStatus("Admin/Screens/Vehicles", model);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "Admin/Login";
    }

}