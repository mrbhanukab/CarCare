package sliit.pg09.carcare.client;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public String verifyUserStatus(String file, Model model) {
        return getCurrentUser()
                .map(user -> {
                    model.addAttribute("user", user);
                    return file;
                })
                .orElse("redirect:/");
    }

    public List<Client> searchClientsByGeneralQuery(String query) {
        if (query == null || query.isBlank()) {
            return clientRepository.findAll(); // Return all clients if query is empty
        }
        return clientRepository.searchByNameEmailOrVehicle(query.toLowerCase());
    }
}

    public void updateClient(Client client) {
        clientRepository.save(client);
    }

    public Optional<Client> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            return Optional.empty();
        }

        String userEmail = oAuth2User.getAttribute("email");
        if (userEmail == null) {
            return Optional.empty();
        }

        return clientRepository.findById(userEmail);
    }

    public boolean createClient (Client client) {
        if(client == null || client.getEmail() == null) {
            return false;
        }

        if(clientRepository.existsById(client.getEmail())) {
            return false;
        }

        clientRepository.save(client);
        return true;
    }

}