package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.sql.Timestamp;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

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

        transactionService.handleWithdrawal(transaction);

//        verify(accountService).updateAccount(argThat(updatedAccount ->
//            "acc101".equals(updatedAccount.getAccountNo()) &&
//            1900.0 == updatedAccount.getBalance() && 
//            "user125".equals(updatedAccount.getOwnerId()) &&
//            "branch101".equals(updatedAccount.getBranchId())
//        ));

        verify(transactionDAO).addTransaction(transaction);
//
//        verify(notificationService).addNotification(any(Notification.class));
    }
    
    
//    @Test
//    void testHandleWithdrawalInsufficientFunds() throws SQLException, ClassNotFoundException {
//        // Arrange
//        String accountId = "123";
//        double amount = 300.0;
//        Account account = new Account();
//        account.setBalance(200.0);
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        transaction.setSourceAccountId(accountId);
//
//        when(accountService.getAccount(accountId)).thenReturn(account);
//
////         Act & Assert
//        assertThrows(InsufficientBalanceException.class, () -> {
//            transactionService.handleWithdrawal(transaction);
//        });
//    }

//    @Test
//    void testHandleDepositSuccess() throws SQLException, ClassNotFoundException {
//        // Arrange
//        String accountId = "456";
//        double amount = 200.0;
//        Account account = new Account();
//        account.setBalance(100.0);
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        transaction.setDestinationAccountId(accountId);
//
//        when(accountService.getAccount(accountId)).thenReturn(account);
//        when(accountService.updateAccount(any(Account.class))).thenReturn(true);
//        when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
//
//        // Act
//        transactionService.handleDeposit(transaction);
//
//        // Assert
//        verify(accountService).updateAccount(account);
//        verify(transactionDAO).addTransaction(transaction);
//        verify(notificationService).addNotification(any(Notification.class));
//    }

//    @Test
//    void testHandleTransferSuccess() throws SQLException, ClassNotFoundException, InsufficientBalanceException {
//        // Arrange
//        String sourceAccountId = "789";
//        String destinationAccountId = "101";
//        double amount = 50.0;
//        Account sourceAccount = new Account();
//        sourceAccount.setBalance(100.0);
//        Account destinationAccount = new Account();
//        destinationAccount.setBalance(200.0);
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        transaction.setSourceAccountId(sourceAccountId);
//        transaction.setDestinationAccountId(destinationAccountId);
//
//        when(accountService.getAccount(sourceAccountId)).thenReturn(sourceAccount);
//        when(accountService.getAccount(destinationAccountId)).thenReturn(destinationAccount);
//        when(accountService.updateAccount(any(Account.class))).thenReturn(true);
//        when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);
//
//        // Act
//        transactionService.handleTransfer(transaction);
//
//        // Assert
//        verify(accountService).updateAccount(sourceAccount);
//        verify(accountService).updateAccount(destinationAccount);
//        verify(transactionDAO).addTransaction(transaction);
//        verify(notificationService).addNotification(any(Notification.class));
//    }
}
