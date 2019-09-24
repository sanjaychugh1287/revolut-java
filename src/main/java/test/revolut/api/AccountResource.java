package test.revolut.api;

import test.revolut.Account;

public class AccountResource {
    private Account account;

    public AccountResource(Account account) {
        this.account = account;
    }

    public long getId() {
        return account.getId();
    }

    public long getAmount() {
        return account.getAmount();
    }

    @Override
    public String toString() {
        return "AccountResource{" +
                "account=" + account +
                '}';
    }
}
