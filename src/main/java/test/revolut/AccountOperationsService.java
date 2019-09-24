package test.revolut;

import test.revolut.tasks.AddTask;
import test.revolut.tasks.TransferTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This service accepts tasks to operate account's money
 * and send them to the multi-thread executor
 */
public class AccountOperationsService {

    private final ExecutorService executorService;

    public AccountOperationsService() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public Future<?> transfer(Account from, Account to, long amount) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Arguments should not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount should be positive");
        }
        return executorService.submit(new TransferTask(from, to, amount));
    }

    public Future<?> add(Account account, long amount) {
        if (account == null) {
            throw new IllegalArgumentException("Arguments should not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount should be positive");
        }
        return executorService.submit(new AddTask(account, amount));
    }

    boolean shutdownAndWaitAllTasks(long timeout, TimeUnit unit) throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(timeout, unit);
        return executorService.isTerminated();
    }
}
