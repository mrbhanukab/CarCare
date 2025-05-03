package sliit.pg09.carcare.emergency;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class EmergencyController {

    @Controller
    @RequestMapping("/client")
    public static class ClientEmergency {
        @Autowired
        private EmergencyService emergencyService;

        @HxRequest
        @GetMapping("/emergency")
        public String showEmergencyModal(Model model) {
            model.addAttribute("vehicle", Map.of(
                    "year", "2024",
                    "brand", "BMW",
                    "model", "X5"
            ));
            return "Client/Components/EmergencyConfirmation :: emergencyConfirmation";
        }

        @HxRequest
        @PostMapping("/emergency")
        @ResponseBody
        public ResponseEntity<String> handleEmergencyRequests(
                @RequestParam Double latitude,
                @RequestParam Double longitude,
                @RequestParam LocalDateTime timestamp,
                @RequestParam String vehicleLicense
        ) {
            try {
                emergencyService.createEmergency(vehicleLicense, latitude, longitude, timestamp);

                HttpHeaders headers = new HttpHeaders();
                System.out.printf("Emergency Request Received: %s, %s, %s%n", latitude, longitude, timestamp);
                headers.add("HX-Redirect", "/client");
                return ResponseEntity.ok()
                        .headers(headers)
                        .body("Emergency request received successfully");

            } catch (Exception e) {
                System.err.println("Error processing emergency request: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process emergency request");
            }
        }
    }

    @Controller
    @RequestMapping("/admin")
    public static class AdminEmergency {
        @Autowired
        private EmergencyService emergencyService;
    }
}