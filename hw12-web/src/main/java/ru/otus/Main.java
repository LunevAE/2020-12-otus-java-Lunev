package ru.otus;

import com.google.gson.GsonBuilder;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.DriverDataSource;
import org.hibernate.cfg.Configuration;
import ru.otus.hibernate.core.repository.DataTemplateHibernate;
import ru.otus.hibernate.core.repository.HibernateUtils;
import ru.otus.hibernate.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.hibernate.crm.model.AddressDataSet;
import ru.otus.hibernate.crm.model.Client;
import ru.otus.hibernate.crm.model.PhoneDataSet;
import ru.otus.hibernate.crm.service.DBServiceClient;
import ru.otus.hibernate.crm.service.DbServiceClientImpl;
import ru.otus.web.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.web.services.ClientAuthServiceImpl;
import ru.otus.web.services.TemplateProcessorImpl;

public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        flywayMigrations(configuration);
        var dbServiceClient = createDbServiceClient(configuration);
        var gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();

        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var authService = new ClientAuthServiceImpl(dbServiceClient);

        var clientWebServer = new ClientsWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceClient, gson, templateProcessor);

        clientWebServer.start();
        clientWebServer.join();
    }

    private static void flywayMigrations(Configuration configuration) {
        var url = configuration.getProperty("hibernate.connection.url");
        var user = configuration.getProperty("hibernate.connection.username");
        var password = configuration.getProperty("hibernate.connection.password");

        var flyway = Flyway.configure()
                .dataSource(new DriverDataSource(Main.class.getClassLoader(), "org.postgresql.Driver", url, user, password))
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }

    private static DBServiceClient createDbServiceClient(Configuration configuration) {
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }
}