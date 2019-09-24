package test.revolut;

import java.util.concurrent.atomic.AtomicLong;

public class Account {
    private final long id;
    private AtomicLong amount = new AtomicLong();

    public Account(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getAmount() {
        return amount.get();
    }

    public boolean applyDelta(long delta) {
        if (delta < 0 && amount.get() < -delta) {
            return false;
        } else {
            amount.addAndGet(delta);
            return true;
        }
    }

}
