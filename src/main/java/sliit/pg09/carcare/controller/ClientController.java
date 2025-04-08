package sliit.pg09.carcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    @GetMapping({"/client", "/client/dashboard"})
    public String getDashboard(Model model) {
        model.addAttribute("active", "Dashboard");
        return "Client/dashboard";
    }

    @GetMapping("/client/garage")
    public String getGarage(Model model) {
        model.addAttribute("active", "Garage");
        return "Client/garage";
    }

    @GetMapping("/client/schedule")
    public String getSchedule(Model model) {
        model.addAttribute("active", "Schedule");
        return "Client/schedule";
    }

    @GetMapping("/client/history")
    public String getHistory(Model model) {
        model.addAttribute("active", "History");
        return "Client/history";
    }

    @GetMapping("/client/vip-contact")
    public String getVipContact(Model model) {
        model.addAttribute("active", "VIP Contact");
        return "Client/vip-contact";
    }

    @GetMapping("/client/account")
    public String getAccount(Model model) {
        model.addAttribute("active", "Account");
        return "Client/account";
    }
}