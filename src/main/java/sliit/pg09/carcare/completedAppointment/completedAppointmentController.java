package sliit.pg09.carcare.completedAppointment;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}