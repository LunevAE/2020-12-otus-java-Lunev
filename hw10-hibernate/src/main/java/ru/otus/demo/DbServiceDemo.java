package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.AddressDataSet;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.PhoneDataSet;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;

public class DbServiceDemo {
    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class,
                AddressDataSet.class,
                PhoneDataSet.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

// client1
        var client1 = new Client("Client");
        var address1 = new AddressDataSet();
        address1.setStreet("Address1");
        var phones1 = new ArrayList<PhoneDataSet>();
        for (int i = 1; i <= 3; i++) {
            phones1.add(new PhoneDataSet("number" + i));
        }
        client1.setAddressDataSet(address1);
        client1.setPhoneDataSet(phones1);

        var savedClient1 = dbServiceClient.saveClient(client1);

// client1 update
        var clientSelected = dbServiceClient.getClient((savedClient1.getId()))
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client1.getId()));
        log.info("clientSelected: {}", clientSelected);

        clientSelected.setName("UpdatedName");
        dbServiceClient.saveClient(clientSelected);
        var clientUpdated = dbServiceClient.getClient(clientSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSelected.getId()));
        log.info("clientUpdated: {}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("clients: {}", client));
    }
}
