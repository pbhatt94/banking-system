package com.wg.banking.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.constants.AccountConstants;
import com.wg.banking.dao.impl.AccountDAOImpl;
import com.wg.banking.model.Account;
import com.wg.banking.model.User;

public class AccountDAOTest {

    private final String accountNo = "acc123";
    private final double balance = 1000.00;
    private final String ownerId = "user1";
    private final String branchId = "branch1";
    private final java.sql.Date createdAt = new java.sql.Date(System.currentTimeMillis());
    private final java.sql.Date updatedAt = new java.sql.Date(System.currentTimeMillis());

    @InjectMocks
    private AccountDAOImpl accountDAO;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testAddAccountSuccess() throws SQLException, ClassNotFoundException {
        Account account = getAccountObj();
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.addAccount(account);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddAccountFailure() throws SQLException, ClassNotFoundException {
        Account account = getAccountObj();
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> accountDAO.addAccount(account));
    }

    @Test
    public void testGetAccountByUser() throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setUserId(ownerId);
        Account account = getAccountObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(AccountConstants.ACCOUNT_NO_COLUMN)).thenReturn(accountNo);
        when(resultSet.getDouble(AccountConstants.BALANCE_COLUMN)).thenReturn(balance);
        when(resultSet.getString(AccountConstants.OWNER_ID_COLUMN)).thenReturn(ownerId);
        when(resultSet.getString(AccountConstants.BRANCH_ID_COLUMN)).thenReturn(branchId);
        when(resultSet.getDate(AccountConstants.CREATED_AT_COLUMN)).thenReturn(createdAt);
        when(resultSet.getDate(AccountConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Account result = accountDAO.getAccount(user);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    public void testGetAccountByAccount() throws SQLException, ClassNotFoundException {
        Account account = getAccountObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(AccountConstants.ACCOUNT_NO_COLUMN)).thenReturn(accountNo);
        when(resultSet.getDouble(AccountConstants.BALANCE_COLUMN)).thenReturn(balance);
        when(resultSet.getString(AccountConstants.OWNER_ID_COLUMN)).thenReturn(ownerId);
        when(resultSet.getString(AccountConstants.BRANCH_ID_COLUMN)).thenReturn(branchId);
        when(resultSet.getDate(AccountConstants.CREATED_AT_COLUMN)).thenReturn(createdAt);
        when(resultSet.getDate(AccountConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Account result = accountDAO.getAccount(account);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    public void testGetAccountByAccountNo() throws SQLException, ClassNotFoundException {
        Account account = getAccountObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(AccountConstants.ACCOUNT_NO_COLUMN)).thenReturn(accountNo);
        when(resultSet.getDouble(AccountConstants.BALANCE_COLUMN)).thenReturn(balance);
        when(resultSet.getString(AccountConstants.OWNER_ID_COLUMN)).thenReturn(ownerId);
        when(resultSet.getString(AccountConstants.BRANCH_ID_COLUMN)).thenReturn(branchId);
        when(resultSet.getDate(AccountConstants.CREATED_AT_COLUMN)).thenReturn(createdAt);
        when(resultSet.getDate(AccountConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Account result = accountDAO.getAccount(accountNo);

        assertEquals(account, result);
    } 

    @Test
    public void testGetAllAccounts() throws SQLException, ClassNotFoundException {
        Account account1 = getAccountObj();
        Account account2 = new Account();
        account2.setAccountNo("acc456");
        account2.setBalance(2000.00);
        account2.setOwnerId("user2");
        account2.setBranchId("branch2");
        account2.setCreatedAt(createdAt);
        account2.setUpdatedAt(updatedAt);

        List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);

        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(AccountConstants.ACCOUNT_NO_COLUMN)).thenReturn(accountNo).thenReturn("acc456");
        when(resultSet.getDouble(AccountConstants.BALANCE_COLUMN)).thenReturn(balance).thenReturn(2000.00);
        when(resultSet.getString(AccountConstants.OWNER_ID_COLUMN)).thenReturn(ownerId).thenReturn("user2");
        when(resultSet.getString(AccountConstants.BRANCH_ID_COLUMN)).thenReturn(branchId).thenReturn("branch2");
        when(resultSet.getDate(AccountConstants.CREATED_AT_COLUMN)).thenReturn(createdAt).thenReturn(createdAt);
        when(resultSet.getDate(AccountConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt).thenReturn(updatedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<Account> result = accountDAO.getAllAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(account1, result.get(0));
        assertEquals(account2, result.get(1));
    }

    @Test
    public void testUpdateAccountSuccess() throws SQLException, ClassNotFoundException {
        Account account = getAccountObj();
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.updateAccount(account);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateAccountFailure() throws SQLException, ClassNotFoundException {
        Account account = getAccountObj();
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> accountDAO.updateAccount(account));
    }

    @Test
    public void testDeleteAccountSuccess() throws SQLException, ClassNotFoundException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = accountDAO.deleteAccount(branchId);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteAccountFailure() throws SQLException, ClassNotFoundException {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> accountDAO.deleteAccount(branchId));
    }

    private Account getAccountObj() {
        Account account = new Account();
        account.setAccountNo(accountNo);
        account.setBalance(balance);
        account.setOwnerId(ownerId);
        account.setBranchId(branchId);
        account.setCreatedAt(createdAt);
        account.setUpdatedAt(updatedAt);
        return account;
    }
}
