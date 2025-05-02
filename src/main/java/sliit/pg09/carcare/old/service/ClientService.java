package sliit.pg09.carcare.old.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.old.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}