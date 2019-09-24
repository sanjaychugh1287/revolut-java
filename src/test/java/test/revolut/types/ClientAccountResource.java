package test.revolut.types;

public class ClientAccountResource {
    private long id;
    private long amount;

    public ClientAccountResource() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
