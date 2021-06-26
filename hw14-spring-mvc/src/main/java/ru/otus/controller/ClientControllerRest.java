package ru.otus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.Client;
import ru.otus.service.ClientServiceImpl;

@RestController
public class ClientControllerRest {

    private final ClientServiceImpl clientService;

    public ClientControllerRest(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public ResponseEntity<Client> findById(@PathVariable long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }
}
