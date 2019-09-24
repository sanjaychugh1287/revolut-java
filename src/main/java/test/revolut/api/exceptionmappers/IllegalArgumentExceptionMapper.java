package test.revolut.api.exceptionmappers;

import test.revolut.api.ErrorResource;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class will convert IllegalArgumentException into correct response with status 400,
 * if it will be thrown from API request.
 */
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResource("illegalArgument", e.getMessage())).build();
    }
}
