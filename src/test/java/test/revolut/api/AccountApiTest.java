package test.revolut.api;

import org.junit.Before;
import org.junit.Test;
import test.revolut.Account;
import test.revolut.AccountOperationsService;
import test.revolut.AccountRegistry;
import test.revolut.exceptions.AccountNotFoundException;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AccountApiTest {

    private AccountRegistry registry;
    private AccountOperationsService operationsService;
    private AccountApi accountApi;

    @Before
    public void setUp() {
        registry = mock(AccountRegistry.class);
        doThrow(AccountNotFoundException.class).when(registry).get(anyLong());
        operationsService = mock(AccountOperationsService.class);
        accountApi = new AccountApi(registry, operationsService);
    }

    @Test
    public void create_shouldReturnNewAccount() {
        doReturn(mockAccount(1)).when(registry).create();
        Response response = accountApi.create();

        assertEquals(response.getStatus(), CREATED.getStatusCode());

        checkResponseAccount(response, 1, 0);
    }

    @Test(expected = AccountNotFoundException.class)
    public void get_shouldThrowException_whenAccountNotExist() {
        accountApi.get(1L);
    }

    @Test
    public void get_shouldReturnAccount_whenExists() {
        doReturn(mockAccount(1)).when(registry).get(1);

        Response response = accountApi.get(1L);

        assertEquals(response.getStatus(), OK.getStatusCode());
        checkResponseAccount(response, 1, 0);
    }

    @Test(expected = AccountNotFoundException.class)
    public void add_shouldThrowException_whenAccountNotExist() {
        accountApi.add(1L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_shouldThrowException_whenAmountIsNegative() {
        doReturn(mockAccount(1)).when(registry).get(1);

        accountApi.add(1L, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_shouldThrowException_whenAmountIsZero() {
        doReturn(mockAccount(1)).when(registry).get(1);

        accountApi.add(1L, 0L);
    }

    @Test
    public void add_shouldAccept_whenRequestIsGood() {
        Account account = mockAccount(1);
        doReturn(account).when(registry).get(1);
        Response response = accountApi.add(1L, 1L);

        assertEquals(response.getStatus(), ACCEPTED.getStatusCode());
        verify(operationsService).add(account, 1);
        verifyNoMoreInteractions(operationsService);
    }

    @Test(expected = AccountNotFoundException.class)
    public void transfer_shouldThrowException_whenAccountNotExists() {
        accountApi.transfer(1L, 2L, 1L);
    }

    @Test(expected = AccountNotFoundException.class)
    public void transfer_shouldThrowException_whenSecondAccountNotExists() {
        doReturn(mockAccount(1)).when(registry).get(1);
        accountApi.transfer(1L, 2L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_shouldThrowException_whenTransferToSameAccount() {
        doReturn(mockAccount(1)).when(registry).get(1);
        accountApi.transfer(1L, 1L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_shouldThrowException_whenAmountIsNegative() {
        doReturn(mockAccount(1)).when(registry).get(1);
        doReturn(mockAccount(2)).when(registry).get(2);

        accountApi.transfer(1L, 2L, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_shouldThrowException_whenAmountIsZero() {
        doReturn(mockAccount(1)).when(registry).get(1);
        doReturn(mockAccount(2)).when(registry).get(2);

        accountApi.transfer(1L, 2L, 0L);
    }

    @Test
    public void transfer_shouldAccept_whenRequestIsGood() {
        Account account1 = mockAccount(1);
        Account account2 = mockAccount(2);

        doReturn(account1).when(registry).get(1);
        doReturn(account2).when(registry).get(2);

        Response response = accountApi.transfer(1L, 2L, 1L);
        assertEquals(response.getStatus(), ACCEPTED.getStatusCode());
        verify(operationsService).transfer(account1, account2, 1);
        verifyNoMoreInteractions(operationsService);
    }

    private void checkResponseAccount(Response response, long id, long amount) {
        Object entity = response.getEntity();
        assertTrue(entity instanceof AccountResource);

        AccountResource resource = (AccountResource) entity;
        assertEquals(amount, resource.getAmount());
        assertEquals(id, resource.getId());
    }

    private Account mockAccount(long id) {
        Account mock = mock(Account.class);
        doReturn(id).when(mock).getId();
        return mock;
    }
}