package test.revolut.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(long id) {
        super("Account with id " + id + " does not exist");
    }

}
