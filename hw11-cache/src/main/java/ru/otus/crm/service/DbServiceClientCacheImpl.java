package ru.otus.crm.service;

import ru.otus.cache.HwCache;
import ru.otus.cache.HwListener;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCacheImpl implements DBServiceClient {
    private final HwCache<String, Client> cache;

    private final DBServiceClient dbServiceClient;

    public DbServiceClientCacheImpl(DbServiceClientImpl dbServiceClient, HwCache<String, Client> cache, HwListener<String, Client>... cacheListeners) {
        this.dbServiceClient = dbServiceClient;
        this.cache = cache;

        for (var cacheListener : cacheListeners) {
            this.cache.addListener(cacheListener);
        }
    }

    @Override
    public Client saveClient(Client client) {
        var savedClient = dbServiceClient.saveClient(client);
        cache.put(String.valueOf(savedClient.getId()), savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var key = String.valueOf(id);
        var clientInCache = cache.get(key);
        if (clientInCache == null) {
            var client = dbServiceClient.getClient(id);
            client.ifPresent(value -> cache.put(key, value));
            return client;
        }

        return Optional.of(clientInCache);
    }

    @Override
    public List<Client> findAll() {
        var clients = dbServiceClient.findAll();
        clients.forEach(it -> cache.put(String.valueOf(it.getId()), it));
        return clients;
    }
}
