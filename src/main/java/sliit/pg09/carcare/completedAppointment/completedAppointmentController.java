package sliit.pg09.carcare.completedAppointment;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class completedAppointmentController {
    @Controller
    @RequestMapping("/client")
    public static class ClientPendingAppointment {
        private final completedAppointmentService completedAppointmentService;

        public ClientPendingAppointment(completedAppointmentService completedAppointmentService) {
            this.completedAppointmentService = completedAppointmentService;
        }

        @HxRequest
        @GetMapping("/completedAppointment")
        public String showCompletedAppointment(@RequestParam String license, Model model) {
            var appointments = completedAppointmentService.getAllAppointmentsSorted(license);
            model.addAttribute("appointments", appointments);
            return "Client/Components/CompletedAppointmentModal";
        }
    }

    @Controller
    @RequestMapping("/admin")
    public static class AdminPendingAppointment {
        private final completedAppointmentService completedAppointmentService;

        public AdminPendingAppointment(completedAppointmentService completedAppointmentService) {
            this.completedAppointmentService = completedAppointmentService;
        }

        @HxRequest
        @PostMapping("/search-completed-appointments")
        public String searchCompletedAppointments(@RequestParam(required = false) String search, Model model) {
            List<completedAppointment> appointments;
            if (search == null || search.isEmpty()) {
                // Load all appointments if no search query is provided
                appointments = completedAppointmentService.getAllAppointments();
            } else {
                // Filter appointments based on the search query
                appointments = completedAppointmentService.searchAppointments(search);
            }
            model.addAttribute("completedAppointments", appointments);
            return "Admin/Components/CompletedAppointmentsTable";
        }
    }
}