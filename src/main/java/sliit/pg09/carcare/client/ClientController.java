package sliit.pg09.carcare.client;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClientController {
    @Controller
    @RequestMapping("/admin")
    public static class Admin {
        private final ClientService clientService;

        public Admin(ClientService clientService) {
            this.clientService = clientService;
        }

        @GetMapping("/clients/search")
        @ResponseBody
        public List<Client> searchClients(@RequestParam(required = false) String query) {
            return clientService.searchClientsByGeneralQuery(query);
        }
    }

    @Controller
    @RequestMapping("/client")
    public static class ClientCC {
        private final ClientService clientService;

        public ClientCC(ClientService clientService) {
            this.clientService = clientService;
        }

        @RequestMapping(value = {"", "/"})
        public String getDashboard(Model model) {
            return clientService.verifyUserStatus("Client/Dashboard", model);
        }

        @HxRequest
        @GetMapping("/account")
        public String showAccountDetails(Model model) {
            return clientService.verifyUserStatus("Client/Components/AccountDetails", model);
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

}