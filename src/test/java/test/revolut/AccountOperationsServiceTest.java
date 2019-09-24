package test.revolut;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountOperationsServiceTest {

    private Account account1;
    private Account account2;
    private AccountOperationsService service;

    @Before
    public void setUp() {
        account1 = new Account(1);
        account2 = new Account(2);
        service = new AccountOperationsService();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenAmountIsNegative() {
        service.transfer(account1, account2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenAmountIsZero() {
        service.transfer(account1, account2, 0);
    }

    @Test(expected = ExecutionException.class)
    public void shouldFail_whenNotEnoughMoney() throws ExecutionException, InterruptedException {
        Future<?> future = service.transfer(account1, account2, 1);
        future.get();
    }

    @Test
    public void shouldTransfer_whenEnoughMoney() throws ExecutionException, InterruptedException {
        account1.applyDelta(1);
        Future<?> future = service.transfer(account1, account2, 1);
        future.get();

        assertEquals(0, account1.getAmount());
        assertEquals(1, account2.getAmount());
    }

    @Test
    public void shouldCompleteAllTransfers_whenTransferToEachOther() throws InterruptedException {
        int initialAmount = 100000;
        account1.applyDelta(initialAmount);
        account2.applyDelta(initialAmount);

        for (int i = 0; i < initialAmount; i++) {
            service.transfer(account1, account2, 1);
            service.transfer(account2, account1, 1);
        }

        boolean isAllCompleted = service.shutdownAndWaitAllTasks(10, TimeUnit.SECONDS);

        assertTrue(isAllCompleted);
        assertEquals(initialAmount, account1.getAmount());
        assertEquals(initialAmount, account2.getAmount());
    }

}