package sliit.pg09.carcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sliit.pg09.carcare.model.Client;
import sliit.pg09.carcare.repository.ClientRepository;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientRepository clients;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("active", "Dashboard");

        var x = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAttributes();

        clients.findById(x.get("email").toString()).ifPresentOrElse(u -> model.addAttribute("user", u), () -> {
            model.addAttribute("user",
                    clients.save(new Client(
                            x.get("name").toString(),
                            x.get("email").toString(),
                            x.get("picture").toString()
                    ))
            );
        });

        return "Client/dashboard";
    }

    @GetMapping("/garage")
    public String getGarage(Model model) {
        model.addAttribute("active", "Garage");
        return "Client/garage";
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        model.addAttribute("active", "Schedule");
        return "Client/schedule";
    }

    @GetMapping("/history")
    public String getHistory(Model model) {
        model.addAttribute("active", "History");
        return "Client/history";
    }

    @GetMapping("/vip-contact")
    public String getVipContact(Model model) {
        model.addAttribute("active", "VIP Contact");
        return "Client/vip-contact";
    }

    @GetMapping("/account")
    public String getAccount(Model model) {
        model.addAttribute("active", "Account");
        return "Client/account";
    }
}