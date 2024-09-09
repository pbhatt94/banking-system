package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.TransactionDAO;
import com.wg.banking.helper.InsufficientBalanceException;
import com.wg.banking.model.Account;
import com.wg.banking.model.Notification;
import com.wg.banking.model.Transaction;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@Mock
	private AccountDAO accountDAO;

	@Mock
	private NotificationDAO notificationDAO;

	@Mock
	private TransactionDAO transactionDAO;

	@Mock
	private AccountService accountService;

	@Mock
	private NotificationService notificationService;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	void testHandleWithdrawalSuccess() throws SQLException, ClassNotFoundException, InsufficientBalanceException {
		// Arrange
		String accountNo = "acc1001";
		String ownerId = "user125";
		String branchId = "branch101";
		Date now = new Date();
		Account account = new Account();
		account.setBalance(2000.0);
		account.setAccountNo(accountNo);
		account.setOwnerId(ownerId);
		account.setBranchId(branchId);
		account.setCreatedAt(now);
		account.setUpdatedAt(now);

		double amount = 100.0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Transaction transaction = new Transaction();
		transaction.setTransactionId("100");
		transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
		transaction.setAmount(amount);
		transaction.setSourceAccountId(accountNo);
		transaction.setCreatedAt(timestamp);
		transaction.setDestinationAccountId(null);

		when(accountService.getAccount(accountNo)).thenReturn(account);
		when(accountService.updateAccount(any(Account.class))).thenReturn(true);
		when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
		when(accountService.getUser(any(String.class))).thenReturn(account);

		transactionService.handleWithdrawal(transaction);

		assertEquals(1900.0, account.getBalance());
		verify(accountService).updateAccount(account);
		verify(transactionDAO).addTransaction(transaction);
		verify(notificationService).addNotification(any(Notification.class));
	}

	@Test
	void testHandleWithdrawalInsufficientFunds() throws SQLException, ClassNotFoundException {
		// Arrange
		String accountNo = "123";
		double amount = 300.0;
		Account account = new Account();
		account.setAccountNo(accountNo);
		account.setBalance(200.0);
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setSourceAccountId(accountNo);

		// Act & Assert
		verify(accountService, never()).updateAccount(any(Account.class));
		verify(transactionDAO, never()).addTransaction(any(Transaction.class));
		verify(notificationService, never()).addNotification(any(Notification.class));
	}

	@Test
	void testHandleDepositSuccess() throws SQLException, ClassNotFoundException {
		// Arrange
		String accountId = "456";
		double amount = 200.0;
		Account account = new Account();
		account.setBalance(100.0);
		account.setAccountNo(accountId);
		account.setOwnerId("user12");
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDestinationAccountId(accountId);
		transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);

		when(accountService.getAccount(accountId)).thenReturn(account);
		when(accountService.updateAccount(any(Account.class))).thenReturn(true);
		when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
		when(accountService.getUser(any(String.class))).thenReturn(account);

		// Act
		transactionService.handleDeposit(transaction);

		// Assert
		assertEquals(300.0, account.getBalance());
		verify(accountService).updateAccount(account);
		verify(transactionDAO).addTransaction(transaction);
		verify(notificationService, times(1)).addNotification(any(Notification.class));
	}

	@Test
	void testHandleTransferSuccess() throws SQLException, ClassNotFoundException, InsufficientBalanceException {
		// Arrange
		String sourceAccountId = "789";
		String destinationAccountId = "101";
		String sourceOwnerId = "user1";
		String destinationOwnerId = "user2";
		double amount = 50.0;
		Account sourceAccount = new Account();
		sourceAccount.setBalance(100.0);
		sourceAccount.setAccountNo(sourceAccountId);
		sourceAccount.setOwnerId(sourceOwnerId);

		Account destinationAccount = new Account();
		destinationAccount.setBalance(200.0);
		destinationAccount.setAccountNo(destinationAccountId);
		sourceAccount.setOwnerId(destinationOwnerId);

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setSourceAccountId(sourceAccountId);
		transaction.setDestinationAccountId(destinationAccountId);
		transaction.setTransactionType(Transaction.TransactionType.TRANSFER);

		when(accountService.getAccount(sourceAccountId)).thenReturn(sourceAccount);
		when(accountService.getAccount(destinationAccountId)).thenReturn(destinationAccount);
		when(accountService.updateAccount(any(Account.class))).thenReturn(true);
		when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
		when(accountService.getUser(sourceAccountId)).thenReturn(sourceAccount);

		// Act
		transactionService.handleTransfer(transaction);

		// Assert
		verify(accountService).updateAccount(sourceAccount);
		verify(accountService).updateAccount(destinationAccount);
		verify(transactionDAO).addTransaction(transaction);
		verify(notificationService).addNotification(any(Notification.class));
	}

	@Test
	void testGetTransactionById() throws SQLException, ClassNotFoundException {
		// Arrange
		String transactionId = "tx123";
		Transaction transaction = new Transaction();
		when(transactionDAO.getTransactionById(transactionId)).thenReturn(transaction);

		// Act
		Transaction result = transactionService.getTransactionById(transactionId);

		// Assert
		assertEquals(transaction, result);
	}

	@Test
	void testGetAllTransactions() throws SQLException, ClassNotFoundException {
		// Arrange
		List<Transaction> transactions = new ArrayList<>();
		when(transactionDAO.getAllTransactions()).thenReturn(transactions);

		// Act
		List<Transaction> result = transactionService.getAllTransactions();

		// Assert
		assertEquals(transactions, result);
	}

	@Test
	void testGetAllTransactionsByBranch() throws SQLException, ClassNotFoundException {
		// Arrange
		String branchId = "branch1";
		List<Transaction> transactions = new ArrayList<>();
		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		account.setBranchId(branchId);
		account.setAccountNo("acc1");
		accounts.add(account);
		when(accountService.getAllAccounts()).thenReturn(accounts);
		when(transactionDAO.getAllTransactions()).thenReturn(transactions);

		// Act
		List<Transaction> result = transactionService.getAllTransactionsByBranch(branchId);

		// Assert
		assertEquals(transactions, result);
	}

}
