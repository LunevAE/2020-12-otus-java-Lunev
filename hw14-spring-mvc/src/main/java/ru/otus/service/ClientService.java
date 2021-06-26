package ru.otus.service;

import ru.otus.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Client findById(long id);
    void save(Client client);
}
