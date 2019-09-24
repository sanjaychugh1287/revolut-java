package test.revolut.tasks;

import org.junit.Before;
import org.junit.Test;
import test.revolut.Account;
import test.revolut.exceptions.NotEnoughMoneyOnAccountException;

import static org.junit.Assert.assertEquals;

public class TransferTaskTest {

    private Account account1;
    private Account account2;

    @Before
    public void setUp() {
        account1 = new Account(1);
        account2 = new Account(2);
    }

    @Test(expected = NotEnoughMoneyOnAccountException.class)
    public void shouldThrowException_whenNotEnoughMoneyToTransfer() {
        new TransferTask(account1, account2, 1).run();
    }

    @Test
    public void shouldTransfer_whenAmountIsCorrectAndEnoughMoney() {
        account1.applyDelta(1);
        new TransferTask(account1, account2, 1).run();

        assertEquals(0, account1.getAmount());
        assertEquals(1, account2.getAmount());
    }
}
