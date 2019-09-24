package test.revolut;

import test.revolut.exceptions.AccountNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AccountRegistry {
    private AtomicLong idCounter = new AtomicLong();
    private Map<Long, Account> accounts = new ConcurrentHashMap<>();

    public Account create() {
        long id = idCounter.incrementAndGet();
        Account account = new Account(id);
        accounts.put(id, account);
        return account;
    }

    public Account get(long id) {
        Account account = accounts.get(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }
}
