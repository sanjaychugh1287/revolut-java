package test.revolut.tasks;

import test.revolut.Account;
import test.revolut.exceptions.NotEnoughMoneyOnAccountException;

/**
 * Task for transferring money from one account to another.
 * We need to lock both accounts while transferring, to prevent any changes in the middle.
 * To prevent dead locks, which can happen, for example, when one thread tries to transfer money
 * from one account to second, and at the same time another thread tries to transfer money backward,
 * we can lock them in ascending order of IDs, to guarantee that no one thread
 * will not try to lock account with greater number before it locked account with smaller number.
 */
public class TransferTask implements Runnable {
    private final Account from;
    private final Account to;
    private final long amount;

    private final Account first;
    private final Account second;

    public TransferTask(Account from, Account to, long amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;

        first = from.getId() < to.getId() ? from : to;
        second = first == from ? to : from;
    }

    @Override
    public void run() {
        synchronized (first) {
            synchronized (second) {
                if (from.applyDelta(-amount)) {
                    to.applyDelta(amount);
                } else {
                    throw new NotEnoughMoneyOnAccountException(from.getId());
                }
            }
        }
    }
}
