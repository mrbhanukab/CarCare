package sliit.pg09.carcare.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.model.Client;
import sliit.pg09.carcare.repository.ClientRepository;

import java.util.List;

@Service
public class ClientServiceImpl {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}