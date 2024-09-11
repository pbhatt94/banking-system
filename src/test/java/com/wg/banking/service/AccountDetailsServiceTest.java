package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wg.banking.dao.AccountDetailsDAO;
import com.wg.banking.model.AccountDetails;

@ExtendWith(MockitoExtension.class)
public class AccountDetailsServiceTest {

	@Mock
	private AccountDetailsDAO accountDetailsDAO;

	@InjectMocks
	private AccountDetailsService accountDetailsService;

//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}

//	@Test
//	void testGetAllAccountDetailsByMinimumBalance() {
//		// Arrange
//		double minBalance = 1000.0;
//		AccountDetails account1 = new AccountDetails();
//		account1.setBalance(1500.0);
//
//		AccountDetails account2 = new AccountDetails();
//		account2.setBalance(500.0);
//
//		List<AccountDetails> accounts = List.of(account1, account2);
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetailsByMinimumBalance(accounts, minBalance);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals(1, result.size());
//		assertTrue(result.contains(account1));
//	}
//	
//	@Test
//	void testGetAllAccountDetailsByMinimumBalance_MinBalanceAboveMaxBalance() {
//		// Arrange
//		double minBalance = 100000.0;
//		AccountDetails account1 = new AccountDetails();
//		account1.setBalance(1500.0);
//		
//		AccountDetails account2 = new AccountDetails();
//		account2.setBalance(500.0);
//		
//		List<AccountDetails> accounts = List.of(account1, account2);
//		
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetailsByMinimumBalance(accounts, minBalance);
//		
//		// Assert
//		assertNotNull(result);
//		assertEquals(0, result.size());
//		assertFalse(result.contains(account1));
//		assertFalse(result.contains(account2));
//	}
//
//	@Test
//	void testGetAllAccountDetailsByMaximumBalance() {
//		// Arrange
//		double maxBalance = 1000.0;
//		AccountDetails account1 = new AccountDetails();
//		account1.setBalance(500.0);
//
//		AccountDetails account2 = new AccountDetails();
//		account2.setBalance(1500.0);
//
//		List<AccountDetails> accounts = List.of(account1, account2);
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetailsByMaximumBalance(accounts, maxBalance);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals(1, result.size());
//		assertTrue(result.contains(account1));
//	}
//	
//	@Test
//	void testGetAllAccountDetailsByMaximumBalance_MaxBalanceBelowMinimumBalance() {
//		// Arrange
//		double maxBalance = 1.0;
//		AccountDetails account1 = new AccountDetails();
//		account1.setBalance(500.0);
//		
//		AccountDetails account2 = new AccountDetails();
//		account2.setBalance(1500.0);
//		
//		List<AccountDetails> accounts = List.of(account1, account2);
//		
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetailsByMaximumBalance(accounts, maxBalance);
//		
//		// Assert
//		assertNotNull(result);
//		assertEquals(0, result.size());
//		assertFalse(result.contains(account1));
//	}
//
//	@Test
//	void testGetAllAccountDetailsByBalanceWithinRange() {
//		// Arrange
//		double minBalance = 500.0;
//		double maxBalance = 1500.0;
//		AccountDetails account1 = new AccountDetails();
//		account1.setBalance(1000.0);
//
//		AccountDetails account2 = new AccountDetails();
//		account2.setBalance(2000.0);
//
//		List<AccountDetails> accounts = List.of(account1, account2);
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetailsByBalanceWithinRange(accounts,
//				minBalance, maxBalance);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals(1, result.size());
//		assertTrue(result.contains(account1));
//	}
//	
//	@Test
//	void testGetAllAccountDetailsByBalanceWithinRange_InvalidRange() {
//		// Arrange
//		double minBalance = 11500.0;
//		double maxBalance = 1500.0;
//		AccountDetails account1 = new AccountDetails();
//		account1.setBalance(1000.0);
//		
//		AccountDetails account2 = new AccountDetails();
//		account2.setBalance(2000.0);
//		
//		List<AccountDetails> accounts = List.of(account1, account2);
//		
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetailsByBalanceWithinRange(accounts,
//				minBalance, maxBalance);
//		
//		// Assert
//		assertNotNull(result);
//		assertEquals(0, result.size());
//		assertFalse(result.contains(account1));
//		assertFalse(result.contains(account2));
//	}
//
//	@Test
//	void testGetAllAccountDetailsByBranchSuccess() throws ClassNotFoundException, SQLException {
//		// Arrange
//		String branchName = "Branch1";
//		AccountDetails account1 = new AccountDetails();
//		account1.setBranch("Branch1");
//
//		AccountDetails account2 = new AccountDetails();
//		account2.setBranch("Branch2");
//
//		List<AccountDetails> accounts = List.of(account1, account2);
//		when(accountDetailsDAO.getAllAccountDetails()).thenReturn(accounts);
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetails(branchName);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals(1, result.size());
//		assertTrue(result.contains(account1));
//	}
//
//	@Test
//	void testGetAllAccountDetailsByBranchException() throws ClassNotFoundException, SQLException {
//		// Arrange
//		String branchName = "Branch1";
//		when(accountDetailsDAO.getAllAccountDetails()).thenThrow(new SQLException());
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetails(branchName);
//
//		// Assert
//		assertNull(result);
//	}
//
//	@Test
//	void testGetAllAccountDetailsSuccess() throws ClassNotFoundException, SQLException {
//		// Arrange
//		AccountDetails account = new AccountDetails();
//		List<AccountDetails> accounts = List.of(account);
//		when(accountDetailsDAO.getAllAccountDetails()).thenReturn(accounts);
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetails();
//
//		// Assert
//		assertNotNull(result);
//		assertEquals(1, result.size());
//		assertTrue(result.contains(account));
//	}
//
//	@Test
//	void testGetAllAccountDetailsException() throws ClassNotFoundException, SQLException {
//		// Arrange
//		when(accountDetailsDAO.getAllAccountDetails()).thenThrow(new SQLException());
//
//		// Act
//		List<AccountDetails> result = accountDetailsService.getAllAccountDetails();
//
//		// Assert
//		assertNull(result);
//	}
}
