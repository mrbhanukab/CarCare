package sliit.pg09.carcare.client;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public String verifyUserStatus(String file, Model model) {
        var userEmail = (((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAttributes()).get("email").toString();
        return clientRepository.findById(userEmail).map(user -> {
            model.addAttribute("user", user);
//            if (user.getNic() == null && !Objects.equals(file, "Client/account")) {
//                return "redirect:/client/account";
//            }
            return file;
        }).orElse("redirect:/");
    }

    public List<Client> searchClientsByGeneralQuery(String query) {
        if (query == null || query.isBlank()) {
            return clientRepository.findAll(); // Return all clients if query is empty
        }
        return clientRepository.searchByNameEmailOrVehicle(query.toLowerCase());
    }
}
