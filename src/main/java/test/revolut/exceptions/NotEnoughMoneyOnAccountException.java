package test.revolut.exceptions;

public class NotEnoughMoneyOnAccountException extends RuntimeException {
    public NotEnoughMoneyOnAccountException(long id) {
        super("Not enough money on account with id " + id);
    }
}
