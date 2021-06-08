package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwListener;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DbServiceClientCacheImpl;
import ru.otus.crm.service.DbServiceClientImpl;
import java.util.ArrayList;

public class HibernateWithCache {
    private static final Logger log = LoggerFactory.getLogger(HibernateWithCache.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, clientId:{}, action: {}", key, value == null ? null : value.getId(), action);
            }
        };

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class,
                AddressDataSet.class,
                PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

// create client
        var client = new Client("Client");
        var address = new AddressDataSet();
        address.setStreet("Address1");
        var phones = new ArrayList<PhoneDataSet>();
        for (int i = 1; i <= 3; i++) {
            phones.add(new PhoneDataSet("number" + i));
        }
        client.setAddressDataSet(address);
        client.setPhoneDataSet(phones);

        var savedClient = dbServiceClient.saveClient(client);

// without cache
        long startTimeWithoutCache = System.nanoTime();
        var client1 = dbServiceClient.getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));
        long timeWithoutCache = System.nanoTime() - startTimeWithoutCache;
        log.info("Without cache. Client: {}, Time: {}", client1, timeWithoutCache);

// with cache
        var dbServiceClientCache = new DbServiceClientCacheImpl(dbServiceClient, new MyCache<>(), listener);
// first take from db
        var clientFromDb = dbServiceClientCache.getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));

        long startTimeWithCache = System.nanoTime();
        var clientFromCache = dbServiceClientCache.getClient(clientFromDb.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientFromDb.getId()));
        long timeWithCache = System.nanoTime() - startTimeWithCache;
        log.info("With cache. Client: {}, Time: {}", clientFromCache, timeWithCache);

        log.info("Time without cache: {}, time with cache: {}. Different: {}",
                timeWithoutCache,
                timeWithCache,
                timeWithoutCache - timeWithCache);
    }
}
