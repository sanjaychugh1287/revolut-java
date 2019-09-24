package test.revolut;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.revolut.types.ClientAccountResource;
import test.revolut.types.ClientErrorResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IntegrationTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() {
        server = Main.startServer();

        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void create_shouldReturnCreated() {
        Response response = target.path("accounts/")
                .request()
                .method("POST");

        assertEquals(CREATED.getStatusCode(), response.getStatus());
        checkAccount(response, 1);
    }

    @Test
    public void get_shouldReturnAccount_whenExists() {
        long accountId = createAccount();

        Response response = target.path("accounts/" + accountId)
                .request()
                .method("GET");

        assertEquals(OK.getStatusCode(), response.getStatus());
        checkAccount(response, accountId);
    }


    @Test
    public void get_shouldReturnNotFound_whenNotExists() {
        Response response = target.path("accounts/1")
                .request()
                .method("GET");

        checkError(response, NOT_FOUND, "accountNotFound");
    }


    @Test
    public void add_shouldReturnNotFound_whenAccountNotExists() {
        Response response = target.path("accounts/1/add")
                .queryParam("amount", 1)
                .request()
                .method("POST");

        checkError(response, NOT_FOUND, "accountNotFound");
    }

    @Test
    public void add_shouldReturnBadRequest_whenWrongAmount() {
        long accountId = createAccount();
        Response response = target.path("accounts/" + accountId + "/add")
                .queryParam("amount", -1)
                .request()
                .method("POST");

        checkError(response, BAD_REQUEST, "illegalArgument");
    }

    @Test
    public void add_shouldReturnAccepted_whenRequestIsGood() {
        long accountId = createAccount();
        Response response = target.path("accounts/" + accountId + "/add")
                .queryParam("amount", 1)
                .request()
                .method("POST");

        assertEquals(ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    public void transfer_shouldReturnAccepted_whenRequestIsGood() {
        long account1 = createAccount();
        long account2 = createAccount();
        add(account1, 1);

        Response response = target.path("accounts/" + account1 + "/transfer")
                .queryParam("to", account2)
                .queryParam("amount", 1)
                .request()
                .method("POST");

        assertEquals(ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    public void transfer_shouldReturnNotFound_whenAccountNotExists() {
        Response response = target.path("accounts/1/transfer")
                .queryParam("to", 2)
                .queryParam("amount", 1)
                .request()
                .method("POST");

        checkError(response, NOT_FOUND, "accountNotFound");
    }

    @Test
    public void transfer_shouldReturnBadRequest_whenTransferToSameAccount() {
        long accountId = createAccount();
        Response response = target.path("accounts/" + accountId + "/transfer")
                .queryParam("to", accountId)
                .queryParam("amount", 1)
                .request()
                .method("POST");

        checkError(response, BAD_REQUEST, "illegalArgument");
    }

    @Test
    public void transfer_shouldReturnBadRequest_whenWrongAmount() {
        long account1 = createAccount();
        long account2 = createAccount();
        Response response = target.path("accounts/" + account1 + "/transfer")
                .queryParam("to", account2)
                .queryParam("amount", -1)
                .request()
                .method("POST");

        checkError(response, BAD_REQUEST, "illegalArgument");
    }

    private void checkAccount(Response response, long accountId) {
        ClientAccountResource accountResource = response.readEntity(ClientAccountResource.class);
        assertNotNull(accountResource);
        assertEquals(accountId, accountResource.getId());
        assertEquals(0, accountResource.getAmount());
    }

    private void checkError(Response response, Response.Status badRequest, String errorCode) {
        assertEquals(badRequest.getStatusCode(), response.getStatus());
        ClientErrorResource error = response.readEntity(ClientErrorResource.class);
        assertNotNull(error);
        assertEquals(errorCode, error.getError());
    }

    private long createAccount() {
        Response response = target.path("accounts/").request().method("POST");
        return response.readEntity(ClientAccountResource.class).getId();
    }

    private void add(long account1, int amount) {
        target.path("accounts/" + account1 + "/add").queryParam("amount", amount).request().method("POST");
    }
}
