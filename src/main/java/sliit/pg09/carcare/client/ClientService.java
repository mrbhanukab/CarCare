package sliit.pg09.carcare.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

}