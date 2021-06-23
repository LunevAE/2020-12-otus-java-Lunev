package ru.otus.web.services;

import ru.otus.hibernate.crm.service.DBServiceClient;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DBServiceClient dbServiceClient;

    public ClientAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
