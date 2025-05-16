package sliit.pg09.carcare.client;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final VehicleService vehicleService;

    public ClientController(ClientService clientService, VehicleService vehicleService) {
        this.clientService = clientService;
        this.vehicleService = vehicleService;
    }

    @RequestMapping(value = {"", "/"})
    public String getDashboard(Model model) {
        var vehicles = vehicleService.getVehiclesByCurrentClient();
        if (!vehicles.isEmpty()) model.addAttribute("vehicle", vehicles.get(0));
        else model.addAttribute("vehicle", null);

        return clientService.verifyUserStatus("Client/Dashboard", model);
    }

    @GetMapping("/search")
    public List<Client> searchClients(@RequestParam(required = false) String query) {
        return clientService.searchClientsByGeneralQuery(query);
    }

    @HxRequest
    @GetMapping("/account")
    public String showAccountDetails(Model model) {
        return clientService.verifyUserStatus("Client/Components/AccountDetails", model);
    }


    @GetMapping("/vehicle/new")  // Changed from nested controller
    public String getAddVehicleModal() {
        return "Client/Components/AddVehicleModal :: addVehicleModal";
    }


    @HxRequest
    @PostMapping("/account")
    public ResponseEntity<Object> updateAccount(@RequestParam String name,
                                                @RequestParam String phone,
                                                @RequestParam String nic,
                                                @RequestParam String address) {
        try {
            return clientService.getCurrentUser()
                    .map(client -> {
                        client.setName(name);
                        client.setPhone(phone);
                        client.setNic(nic);
                        client.setAddress(address);
                        clientService.updateClient(client);
                        return ResponseEntity.ok()
                                .header("HX-Trigger", "showToast")
                                .header("HX-Trigger-After-Settle", "closeModal")
                                .build();
                    })
                    .orElseGet(() -> ResponseEntity.ok()
                            .header("HX-Trigger", "showError")
                            .body("Not authorized to perform this action"));
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .header("HX-Trigger", "showError")
                    .body("An error occurred while updating your account");
        }
    }

    @HxRequest
    @PostMapping("/create")
    public ResponseEntity<Object> createClient(@RequestParam String email,
                                               @RequestParam String name,
                                               @RequestParam String phone,
                                               @RequestParam String address) {
        Client newClient = new Client();
        newClient.setEmail(email);
        newClient.setName(name);
        newClient.setPhone(phone);
        newClient.setAddress(address);

        try {
            clientService.createClient(newClient);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok()
                    .body("<div class='alert alert-danger'>Invalid client details provided</div>");
        } catch (IllegalStateException e) {
            return ResponseEntity.ok()
                    .body("<div class='alert alert-danger'>A client with this email already exists</div>");
        }
    }
}