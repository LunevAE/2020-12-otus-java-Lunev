package ru.otus.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.hibernate.crm.model.AddressDataSet;
import ru.otus.hibernate.crm.model.Client;
import ru.otus.hibernate.crm.model.PhoneDataSet;
import ru.otus.hibernate.crm.service.DBServiceClient;
import ru.otus.web.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class NewClientServlet extends HttpServlet {

    private static final String NEW_CLIENT_PAGE_TEMPLATE = "new-client.html";

    private final transient TemplateProcessor templateProcessor;
    private final transient DBServiceClient dbServiceClient;

    public NewClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(NEW_CLIENT_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var client = createClient(request.getParameterMap());
        dbServiceClient.saveClient(client);

        response.sendRedirect("/clients");
    }

    private Client createClient(Map<String, String[]> body) {
        var name = body.get("name")[0];
        var login = body.get("login")[0];
        var password = body.get("password")[0];
        var address = body.get("address")[0];
        var phone = body.get("phone")[0];
        var client = new Client(name, login, password);

        var addressDataSet1 = new AddressDataSet();
        addressDataSet1.setStreet(address);
        client.setAddressDataSet(addressDataSet1);
        addressDataSet1.setClient(client);

        var phoneDataSet1 = new PhoneDataSet(phone);
        phoneDataSet1.setClient(client);

        client.setPhoneDataSet(List.of(phoneDataSet1));

        return client;
    }
}
