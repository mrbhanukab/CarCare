package sliit.pg09.carcare.client;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
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
                                                @RequestParam String address) {
        System.out.printf("Updating account details: %s, %s, %s%n", name, phone, address);
        try {
            System.out.println("Current User: " + clientService.getCurrentUser());
            return clientService.getCurrentUser()
                    .map(client -> {
                        client.setName(name);
                        client.setPhone(phone);
                        client.setAddress(address);
                        clientService.updateClient(client);
                        return ResponseEntity.ok().build();
                    })
                    .orElseGet(() -> ResponseEntity.ok()
                            .body("""
                                    <div th:replace="~{Error :: error('Not authorized to perform this action')}"></div>
                                    """));
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body("""
                            <div th:replace="~{Error :: error('An error occurred while updating your account')}"></div>
                            """);
        }
    }

    @HxRequest
    @PostMapping("/create")
    public ResponseEntity<Object> createClient(@RequestParam String email,
                                               @RequestParam String name,
                                               @RequestParam String phone,
                                               @RequestParam String address){
        Client newClient = new Client();
        newClient.setEmail(email);
        newClient.setName(name);
        newClient.setPhone(phone);
        newClient.setAddress(address);

        if(clientService.createClient(newClient))
            return ResponseEntity.ok()
                    .body("<div class= 'alert-success'>Client created successfully.<div>");

        else
            return ResponseEntity.ok()
                    .body("<div class= 'alert alert-danger'>Client could not be created successfully. Email may already exist.<div>");
    }

}