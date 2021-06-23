package ru.otus.web.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.hibernate.crm.service.DBServiceClient;
import ru.otus.web.services.TemplateProcessor;
import ru.otus.web.services.ClientAuthService;
import ru.otus.web.servlet.AuthorizationFilter;
import ru.otus.web.servlet.LoginServlet;

import java.util.Arrays;

public class ClientsWebServerWithFilterBasedSecurity extends ClientsWebServerSimple {
    private final ClientAuthService authService;

    public ClientsWebServerWithFilterBasedSecurity(int port,
                                                   ClientAuthService authService,
                                                   DBServiceClient dbServiceClient,
                                                   Gson gson,
                                                   TemplateProcessor templateProcessor) {
        super(port, dbServiceClient, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
