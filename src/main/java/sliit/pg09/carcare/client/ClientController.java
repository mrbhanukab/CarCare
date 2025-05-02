package sliit.pg09.carcare.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;

    @RequestMapping(value = {"", "/"})
    public String getDashboard(Model model) {
        return clientService.verifyUserStatus("Client/Dashboard", model);
    }
}
