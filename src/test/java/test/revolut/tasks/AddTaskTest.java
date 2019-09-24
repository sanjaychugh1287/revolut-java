package test.revolut.tasks;

import org.junit.Test;
import test.revolut.Account;

import static org.junit.Assert.assertEquals;

public class AddTaskTest {

    @Test
    public void shouldAdd_whenTaskComplete() {
        Account account = new Account(1);

        new AddTask(account, 1).run();

        assertEquals(1, account.getAmount());
    }

}