package ru.otus.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exception.ClientNotFoundException;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Not found client by id: " + id));
    }

    @Override
    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }
}
