package test.revolut;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import test.revolut.api.AccountApi;
import test.revolut.api.exceptionmappers.AccountNotFoundExceptionMapper;
import test.revolut.api.exceptionmappers.IllegalArgumentExceptionMapper;

import java.net.URI;

public class Main {

    static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {
        startServer();
    }

    static HttpServer startServer() {
        AccountRegistry accountRegistry = new AccountRegistry();
        AccountOperationsService accountOperationsService = new AccountOperationsService();

        ResourceConfig config = new ResourceConfig()
                .register(new AccountApi(accountRegistry, accountOperationsService))
                .register(new AccountNotFoundExceptionMapper())
                .register(new IllegalArgumentExceptionMapper())
                .register(JacksonJsonProvider.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

}

