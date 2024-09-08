package com.wg.banking_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.TransactionDAO;
import com.wg.banking.helper.InsufficientBalanceException;
import com.wg.banking.model.Account;
import com.wg.banking.model.Notification;
import com.wg.banking.model.Transaction;
import com.wg.banking.service.AccountService;
import com.wg.banking.service.NotificationService;
import com.wg.banking.service.TransactionService;

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

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
	    

	    @Test
	    void testHandleWithdrawalSuccess() throws SQLException, ClassNotFoundException, InsufficientBalanceException {
	        String accountNo = "acc101";
	        String owner_id = "user125";
	        String branch_id = "branch101";
	        Account account = new Account();
	        account.setBalance(2000.0); // Initial balance
	        account.setAccountNo(accountNo);
	        account.setOwnerId(owner_id);
	        account.setBranchId(branch_id);
	        
	        double amount = 100.0;
	        Transaction transaction = new Transaction();
	        transaction.setTransactionId("100");
	        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
	        transaction.setAmount(amount);
	        transaction.setSourceAccountId(accountNo);

	        when(accountService.getAccount(accountNo)).thenReturn(account);
	        when(accountService.updateAccount(any(Account.class))).thenReturn(true);
	        when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);

	        transactionService.handleWithdrawal(transaction);   
	        
	        
	        assertEquals(1900.0, account.getBalance());
	        
	        
//	        verify(accountService).updateAccount(argThat(updatedAccount ->
//	            "acc101".equals(updatedAccount.getAccountNo()) &&
//	            91.0 == updatedAccount.getBalance() && // Expected balance after withdrawal
//	            "user125".equals(updatedAccount.getOwnerId()) &&
//	            "branch101".equals(updatedAccount.getBranchId())
//	        ));
//	        verify(transactionDAO).addTransaction(transaction);
//	        verify(notificationService).addNotification(any(Notification.class));
	    }
	    
	    
//	    @Test
//	    void testHandleDepositSuccess() throws SQLException, ClassNotFoundException {
//	        String accountNo = "acc101";
//	        String owner_id = "user125";
//	        String branch_id = "branch101";
//	        Account account = new Account();
//	        account.setBalance(2000.0); // Initial balance
//	        account.setAccountNo(accountNo);
//	        account.setOwnerId(owner_id);
//	        account.setBranchId(branch_id);
//	        
//	        double amount = 200.0;
//	        Transaction transaction = new Transaction();
//	        transaction.setAmount(amount);
//	        transaction.setDestinationAccountId(accountNo);
//
//	        when(accountService.getAccount(accountNo)).thenReturn(account);
//	        when(accountService.updateAccount(any(Account.class))).thenReturn(true);
//	        when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
//
//	        transactionService.handleDeposit(transaction);
//
//	        verify(accountService).updateAccount(account);
//	        verify(transactionDAO).addTransaction(transaction);
//	        verify(notificationService).addNotification(any(Notification.class));
//	    }
//	    
//	    
//	    @Test
//	    void testHandleTransferSuccess() throws SQLException, ClassNotFoundException, InsufficientBalanceException {
//	        String sourceAccountId = "789";
//	        String destinationAccountId = "101";
//	        double amount = 50.0;
//	        Account sourceAccount = new Account();
//	        sourceAccount.setBalance(100.0);
//	        Account destinationAccount = new Account();
//	        destinationAccount.setBalance(200.0);
//	        Transaction transaction = new Transaction();
//	        transaction.setAmount(amount);
//	        transaction.setSourceAccountId(sourceAccountId);
//	        transaction.setDestinationAccountId(destinationAccountId);
//
//	        when(accountService.getAccount(sourceAccountId)).thenReturn(sourceAccount);
//	        when(accountService.getAccount(destinationAccountId)).thenReturn(destinationAccount);
//	        when(accountService.updateAccount(any(Account.class))).thenReturn(true);
//	        when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
//
//	  
//	        transactionService.handleTransfer(transaction);
//
//	        verify(accountService).updateAccount(sourceAccount);
//	        verify(accountService).updateAccount(destinationAccount);
//	        verify(transactionDAO).addTransaction(transaction);
//	        verify(notificationService).addNotification(any(Notification.class));
//	    }
//
//	    @Test
//	    void testGetTransactionById() throws SQLException, ClassNotFoundException {
//	        String transactionId = "tx123";
//	        Transaction transaction = new Transaction();
//	        when(transactionDAO.getTransactionById(transactionId)).thenReturn(transaction);
//
//	        Transaction result = transactionService.getTransactionById(transactionId);
//
//	        assertEquals(transaction, result);
//	    }
//
//	    @Test
//	    void testGetAllTransactions() throws SQLException, ClassNotFoundException {
//	        List<Transaction> transactions = new ArrayList<>();
//	        when(transactionDAO.getAllTransactions()).thenReturn(transactions);
//
//	        List<Transaction> result = transactionService.getAllTransactions();
//
//	        assertEquals(transactions, result);
//	    }
}
