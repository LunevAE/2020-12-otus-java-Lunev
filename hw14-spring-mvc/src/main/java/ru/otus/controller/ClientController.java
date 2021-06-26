package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientDto;
import ru.otus.model.AddressDataSet;
import ru.otus.model.Client;
import ru.otus.model.PhoneDataSet;
import ru.otus.service.ClientServiceImpl;

import java.util.List;
import java.util.Set;

@Controller
public class ClientController {

    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/client/list")
    public String clients(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        var attributeValue = new ClientDto();
        model.addAttribute("client", attributeValue);
        return "new-client";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientDto client) {
        var phoneDataSet = new PhoneDataSet(client.getPhone());
        var addressDataSet = new AddressDataSet(client.getAddress());

        var newClient = new Client(null, client.getName(), "", "", addressDataSet, Set.of(phoneDataSet));

        clientService.save(newClient);
        return new RedirectView("/client/list", true);
    }

}
