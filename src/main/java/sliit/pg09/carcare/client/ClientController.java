package sliit.pg09.carcare.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Controller
@RestController
@RequestMapping("/client")
public class ClientController {
    
    @Autowired
    private ClientService clientService;

    @RequestMapping(value = {"", "/"})
    public String getDashboard(Model model) {
        return clientService.verifyUserStatus("Client/Dashboard", model);
    }

    @GetMapping("/search")
    public List<Client> searchClients(@RequestParam(required = false) String query) {
        return clientService.searchClientsByGeneralQuery(query);
    }
}
