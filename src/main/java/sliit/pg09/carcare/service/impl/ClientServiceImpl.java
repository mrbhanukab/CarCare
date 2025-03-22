package sliit.pg09.carcare.service.impl;

import sliit.pg09.carcare.dta.ClientDto;
import sliit.pg09.carcare.model.Client;
import sliit.pg09.carcare.repository.ClientRepository;
import sliit.pg09.carcare.service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<ClientDto> findAll() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map((this::mapToClientDto)).collect(Collectors.toList());
    }

    //! all `mapToDto`
    private ClientDto mapToClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .nic(client.getNic())
                .created(client.getCreated())
                .updated(client.getUpdated())
                .build();
    }
}
