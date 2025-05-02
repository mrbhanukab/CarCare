package sliit.pg09.carcare.controller;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.repository.ClientRepository;

import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientRepository clients;

    private String manageUser(String file, Model model) {
        var userEmail = (((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAttributes()).get("email").toString();
        return clients.findById(userEmail).map(user -> {
            model.addAttribute("user", user);
            if (user.getNic() == null && !Objects.equals(file, "Client/account")) {
                return "redirect:/client/account";
            }
            return file;
        }).orElse("redirect:/");
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("active", "Dashboard");
        return manageUser("Client/Dashboard", model);
    }

    @HxRequest
    @GetMapping("/emergency")
    public String showEmergencyModal(Model model) {
        System.out.println("Emergency Button Clicked");
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
    public ResponseEntity<String> handleEmergency(@RequestParam Double latitude,
                                                  @RequestParam Double longitude,
                                                  @RequestParam String timestamp) {
        try {
            // Return success response with HTMX headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("HX-Redirect", "/client/dashboard"); // Redirect to dashboard after success
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("Emergency request received successfully");

        } catch (Exception e) {
            // Log the error
            System.err.println("Error processing emergency request: " + e.getMessage());

            // Return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process emergency request");
        }
    }

    @HxRequest
    @GetMapping("/account-details")
    public String showAccountDetails(Model model) {
        var userEmail = (((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAttributes()).get("email").toString();

        return clients.findById(userEmail).map(user -> {
            model.addAttribute("user", user);
            return "Client/Components/AccountDetails :: accountDetails";
        }).orElse("redirect:/");
    }

    @HxRequest
    @PostMapping("/account/update")
    public ResponseEntity<String> updateAccount(@RequestParam String name,
                                                @RequestParam String phone,
                                                @RequestParam String address) {
        try {
//            var userEmail = (((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                    .getAttributes()).get("email").toString();
//
//            var client = clients.findById(userEmail).orElseThrow();
//            client.setName(name);
//            client.setPhone(phone);
//            client.setAddress(address);
//            clients.save(client);

            System.out.println("Account updated successfully:");
            System.out.println("Name: " + name);
            System.out.println("Phone: " + phone);
            System.out.println("Address: " + address);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error updating account: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/schedule")
    @HxRequest
    public String showScheduleModal(Model model) {
        model.addAttribute("vehicle", Map.of(
                "year", "2024",
                "brand", "BMW",
                "model", "X5",
                "image", "https://images-porsche.imgix.net/-/media/5D0BB7E042BD4C9DBEF84B5E70482520_73AA748306934B0C9CE20E32231DFCE2_CZ25W01IX0011911-carrera-front?w=750&q=85&auto=format"
        ));
        return "Client/Components/ScheduleServiceModal :: scheduleService";
    }

    @HxRequest
    @GetMapping("/history")
    public String showHistoryModal(Model model) {
        model.addAttribute("vehicle", Map.of(
                "year", "2024",
                "brand", "BMW",
                "model", "X5"
        ));
        return "Client/Components/AppointmentHistoryModal :: appointmentHistory";
    }
}