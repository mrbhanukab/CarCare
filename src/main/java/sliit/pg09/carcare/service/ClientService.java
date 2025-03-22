package sliit.pg09.carcare.service;

import org.springframework.stereotype.Service;
import sliit.pg09.carcare.dta.ClientDto;

import java.util.List;

@Service
public interface ClientService {
    List<ClientDto> findAll();
}
