package sliit.pg09.carcare.emergency;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;

@Controller
public class EmergencyController {
    @Autowired
    EmergencyService emergencyService;

    @Controller
    @RequestMapping("/client")
    public static class AClientEmergency {
        @Autowired
        private EmergencyController emergencyController;
        @Autowired
        private EmergencyService emergencyService;
        @Autowired
        private VehicleService vehicleService;

        @HxRequest
        @GetMapping("/emergency")
        public String getEmergencies(@RequestParam String license, Model model) {
            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            model.addAttribute("vehicle", vehicle);
            return "Client/Components/EmergencyConfirmation";
        }

        @HxRequest
        @PostMapping("/emergency")
        public String createEmergency(
                @RequestParam String license,
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp,
                @RequestParam double latitude,
                @RequestParam double longitude,
                Model model, HttpServletResponse response) {

            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            if (vehicle == null) {
                model.addAttribute("message", "Vehicle not found");
                return "Components/Error :: error";
            }

            emergencyService.createEmergency(vehicle, timestamp, latitude, longitude);

            response.setHeader("HX-Trigger", "closeModal");
            return null;
        }
    }

    @Controller
    @RequestMapping("/admin")
    public static class AdminEmergency {
        @Autowired
        private EmergencyController emergencyController;
        @Autowired
        private EmergencyService emergencyService;

        @PostMapping("/emergency/handle")
        public String markAsHandled(
                @RequestParam String license,
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time,
                Model model) {

            boolean success = emergencyService.markRequestAsHandled(license, time);

            if (!success) {
                model.addAttribute("message", "Emergency request not found or already handled");
                return "Components/Error :: error";
            }

            model.addAttribute("activeCount", emergencyService.getActiveEmergencies().size());
            return "Admin/Components/EmergencyCounter :: counterElement";
        }
    }
}