package test.revolut.tasks;

import test.revolut.Account;

public class AddTask implements Runnable {

    private final Account account;
    private final long amount;

    public AddTask(Account account, long amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        synchronized (account) {
            account.applyDelta(amount);
        }
    }
}
