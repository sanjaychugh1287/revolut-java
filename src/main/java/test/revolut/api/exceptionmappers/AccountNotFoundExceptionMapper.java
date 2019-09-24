package test.revolut.api.exceptionmappers;

import test.revolut.api.ErrorResource;
import test.revolut.exceptions.AccountNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class will convert AccountNotFoundException into correct response with status 404,
 * if it will be thrown from API request.
 */
@Provider
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {
    @Override
    public Response toResponse(AccountNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResource("accountNotFound", e.getMessage())).build();
    }
}
