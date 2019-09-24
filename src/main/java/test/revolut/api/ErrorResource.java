package test.revolut.api;

public class ErrorResource {
    private final String error;
    private final String errorDescription;

    public ErrorResource(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
