package ru.otus.web.services;

public interface ClientAuthService {
    boolean authenticate(String login, String password);
}
