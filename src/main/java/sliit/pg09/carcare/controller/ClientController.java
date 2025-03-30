package sliit.pg09.carcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {
    @GetMapping("/client")
    public String client() {
        return "Client/index";
    }

    @PostMapping("/client/wrong-email")
    public ResponseEntity<String> wrongEmail() {
        System.out.println("Wrong email");
        return ResponseEntity.ok().body("<meta http-equiv=\"refresh\" content=\"0\">");
    }

    @PostMapping("/client/otp")
    public String sendOTP(@RequestParam("email") String email, Model model) {
        // Print email to console
        System.out.println("Email received: " + email);

        // Generate OTP (for simplicity, using a fixed OTP)
        String otp = "123456";

        // Add email and OTP to the model
        model.addAttribute("email", email);
        model.addAttribute("otp", otp);

        return "Home/otp";
    }

    @PostMapping("/client/auth")
    public ResponseEntity<String> authenticate(@RequestParam("otp1") String otp1,
                                               @RequestParam("otp2") String otp2,
                                               @RequestParam("otp3") String otp3,
                                               @RequestParam("otp4") String otp4,
                                               @RequestParam("otp5") String otp5,
                                               @RequestParam("otp6") String otp6) {
        // Concatenate OTP parts
        String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;

        // Print OTP to console
        System.out.println("OTP received: " + otp);

        // Redirect to /client
        return ResponseEntity.ok().body("<meta http-equiv=\"refresh\" content=\"0; URL=/client\">");
    }
}