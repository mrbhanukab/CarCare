package sliit.pg09.carcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sliit.pg09.carcare.repository.ClientRepository;

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
        return manageUser("Client/dashboard", model);
    }

    @GetMapping("/garage")
    public String getGarage(Model model) {
        model.addAttribute("active", "Garage");
        return manageUser("Client/garage", model);
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        model.addAttribute("active", "Schedule");
        return manageUser("Client/schedule", model);
    }

    @GetMapping("/history")
    public String getHistory(Model model) {
        model.addAttribute("active", "History");
        return manageUser("Client/history", model);
    }

    @GetMapping("/vip-contact")
    public String getVipContact(Model model) {
        model.addAttribute("active", "VIP Contact");
        return manageUser("Client/vip-contact", model);
    }

    @GetMapping("/account")
    public String getAccount(Model model) {
        model.addAttribute("active", "Account");
        return manageUser("Client/account", model);
    }
}