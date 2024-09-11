package com.wg.banking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.dao.AccountDAO;
import com.wg.banking.model.Account;
import com.wg.banking.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

	@Mock
	private AccountDAO accountDAO;

	@InjectMocks
	private AccountService accountService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllAccounts_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		List<Account> accounts = new ArrayList<>();
		Account account1 = new Account();
		account1.setAccountNo("123");
		Account account2 = new Account();
		account2.setAccountNo("456");
		accounts.add(account1);
		accounts.add(account2);

		when(accountDAO.getAllAccounts()).thenReturn(accounts);

		// Act
		List<Account> result = accountService.getAllAccounts();

		// Assert
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("123", result.get(0).getAccountNo());
		assertEquals("456", result.get(1).getAccountNo());
	}

	@Test
	void testGetAllAccounts_Exception() throws SQLException, ClassNotFoundException {
		// Arrange
		when(accountDAO.getAllAccounts()).thenThrow(new SQLException("Database error"));

		// Act
		List<Account> result = accountService.getAllAccounts();

		// Assert
		assertNull(result);
	}

	@Test
	void testAddAccount_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		Account newAccount = new Account();
		newAccount.setAccountNo("789");
		newAccount.setOwnerId("owner1");

		when(accountService.getAccount("789")).thenReturn(null);
		when(accountService.getAllAccounts()).thenReturn(new ArrayList<>());

		// Act
		boolean result = accountService.addAccount(newAccount);

		// Assert
		assertTrue(result);
		verify(accountDAO).addAccount(newAccount);
	}

	@Test
	void testAddAccount_AlreadyExists() throws SQLException, ClassNotFoundException {
		// Arrange
		Account existingAccount = new Account();
		existingAccount.setAccountNo("789");

		when(accountService.getAccount("789")).thenReturn(existingAccount);

		// Act
		boolean result = accountService.addAccount(existingAccount);

		// Assert
		assertFalse(result);
		verify(accountDAO, never()).addAccount(any(Account.class));
	}

	@Test
	void testUpdateAccount_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		Account accountToUpdate = new Account();
		accountToUpdate.setAccountNo("789");

		when(accountDAO.updateAccount(accountToUpdate)).thenReturn(true);

		// Act
		boolean result = accountService.updateAccount(accountToUpdate);

		// Assert
		assertTrue(result);
	}

	@Test
	void testUpdateAccount_Failure() throws SQLException, ClassNotFoundException {
		// Arrange
		Account accountToUpdate = new Account();
		accountToUpdate.setAccountNo("789");

		when(accountDAO.updateAccount(accountToUpdate)).thenReturn(false);

		// Act
		boolean result = accountService.updateAccount(accountToUpdate);

		// Assert
		assertFalse(result);
	}

	@Test
	void testDeleteAccount_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		String branchId = "branch1";

		// Act
		accountService.deleteAccount(branchId);

		// Assert
		verify(accountDAO).deleteAccount(branchId);
	}

	@Test
	void testGetAccount_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		Account account = new Account();
		account.setAccountNo("123");

		when(accountDAO.getAccount("123")).thenReturn(account);

		// Act
		Account result = accountService.getAccount("123");

		// Assert
		assertNotNull(result);
		assertEquals("123", result.getAccountNo());
	}

	@Test
	void testGetAccount_Exception() throws SQLException, ClassNotFoundException {
		// Arrange
		when(accountDAO.getAccount("123")).thenThrow(new SQLException("Database error"));

		// Act
		Account result = accountService.getAccount("123");

		// Assert
		assertNull(result);
	}

	@Test
	void testGetAccountByUser_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		User user = new User();
		user.setUserId("user1");
		Account account = new Account();
		account.setAccountNo("123");

		when(accountDAO.getAccount(user)).thenReturn(account);

		// Act
		Account result = accountService.getAccount(user);

		// Assert
		assertNotNull(result);
		assertEquals("123", result.getAccountNo());
	}

	@Test
	void testGetAccountByUser_Exception() throws SQLException, ClassNotFoundException {
		// Arrange
		User user = new User();
		user.setUserId("user1");

		when(accountDAO.getAccount(user)).thenThrow(new SQLException("Database error"));

		// Act
		Account result = accountService.getAccount(user);

		// Assert
		assertNull(result);
	}
}
