package test.revolut.api;

import org.glassfish.jersey.internal.guava.Preconditions;
import test.revolut.Account;
import test.revolut.AccountOperationsService;
import test.revolut.AccountRegistry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApi {

    private final AccountRegistry accountRegistry;
    private final AccountOperationsService accountOperationsService;

    public AccountApi(AccountRegistry accountRegistry, AccountOperationsService accountOperationsService) {
        this.accountRegistry = accountRegistry;
        this.accountOperationsService = accountOperationsService;
    }

    @POST
    @Path("/")
    public Response create() {
        Account account = accountRegistry.create();
        return Response.status(Response.Status.CREATED).entity(new AccountResource(account)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Account account = accountRegistry.get(id);
        return Response.status(Response.Status.OK).entity(new AccountResource(account)).build();
    }

    @POST
    @Path("/{id}/add")
    public Response add(@PathParam("id") Long id, @QueryParam("amount") Long amount) {
        Account account = accountRegistry.get(id);

        Preconditions.checkArgument(amount != null, "Amount should not be null");
        Preconditions.checkArgument(amount > 0, "Amount should be positive");

        accountOperationsService.add(account, amount);

        return Response.status(Response.Status.ACCEPTED).build();
    }


    @POST
    @Path("/{id}/transfer")
    public Response transfer(@PathParam("id") Long from, @QueryParam("to") Long to, @QueryParam("amount") Long amount) {
        Account accountFrom = accountRegistry.get(from);
        Account accountTo = accountRegistry.get(to);

        Preconditions.checkArgument(accountFrom != accountTo, "Accounts should be different");
        Preconditions.checkArgument(amount != null, "Amount should not be null");
        Preconditions.checkArgument(amount > 0, "Amount should be positive");

        accountOperationsService.transfer(accountFrom, accountTo, amount);

        return Response.status(Response.Status.ACCEPTED).build();
    }
}
