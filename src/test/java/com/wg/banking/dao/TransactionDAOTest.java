package com.wg.banking.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.constants.TransactionConstants;
import com.wg.banking.dao.impl.TransactionDAOImpl;
import com.wg.banking.model.Transaction;

public class TransactionDAOTest {
	private final String transactionId = "txn123";
	private final double amount = 500.0;
	private final String sourceAccountId = "acc123";
	private final String destinationAccountId = "acc456";
	private final String transactionType = "DEPOSIT";
	private final String createdAt = "2024-09-10 12:00:00";

	@InjectMocks
	private TransactionDAOImpl transactionDAO;

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
	public void testAddTransactionSuccess_Withdrawal() throws SQLException, ClassNotFoundException {
		Transaction transaction = getTransactionObj();
		transaction.setTransactionType(Transaction.TransactionType.valueOf("WITHDRAWAL"));

		when(preparedStatement.executeUpdate()).thenReturn(1);

		boolean result = transactionDAO.addTransaction(transaction);

		assertTrue(result);
		verify(preparedStatement).executeUpdate();
	}

	@Test
	public void testAddTransactionSuccess_Deposit() throws SQLException, ClassNotFoundException {
		Transaction transaction = getTransactionObj();
		transaction.setTransactionType(Transaction.TransactionType.valueOf("DEPOSIT"));

		when(preparedStatement.executeUpdate()).thenReturn(1);

		boolean result = transactionDAO.addTransaction(transaction);

		assertTrue(result);
		verify(preparedStatement).executeUpdate();
	}

	@Test
	public void testAddTransactionSuccess_Transfer() throws SQLException, ClassNotFoundException {
		Transaction transaction = getTransactionObj();
		transaction.setTransactionType(Transaction.TransactionType.valueOf("TRANSFER"));

		when(preparedStatement.executeUpdate()).thenReturn(1);

		boolean result = transactionDAO.addTransaction(transaction);

		assertTrue(result);
		verify(preparedStatement).executeUpdate();
	}

	@Test
	public void testGetTransactionById() throws SQLException, ClassNotFoundException {
		Transaction transaction = getTransactionObj();

		when(resultSet.next()).thenReturn(true).thenReturn(false); // Simulate one result
		when(resultSet.getString(TransactionConstants.TRANSACTION_ID_COLUMN)).thenReturn(transactionId);
		when(resultSet.getDouble(TransactionConstants.AMOUNT_COLUMN)).thenReturn(amount);
		when(resultSet.getString(TransactionConstants.TRANSACTION_TYPE_COLUMN)).thenReturn(transactionType);
		when(resultSet.getString(TransactionConstants.SOURCE_ACCOUNT_ID_COLUMN)).thenReturn(sourceAccountId);
		when(resultSet.getString(TransactionConstants.DESTINATION_ACCOUNT_ID_COLUMN)).thenReturn(destinationAccountId);
		when(resultSet.getTimestamp(TransactionConstants.CREATED_AT_COLUMN)).thenReturn(Timestamp.valueOf(createdAt));

		when(preparedStatement.executeQuery()).thenReturn(resultSet);

		Transaction result = transactionDAO.getTransactionById(transactionId);

		assertNotNull(result);
		assertEquals(transaction, result);
	}

	@Test
	public void testGetAllTransactions() throws SQLException, ClassNotFoundException {
		Transaction transaction1 = getTransactionObj();
		Transaction transaction2 = new Transaction();
		transaction2.setTransactionId("txn124");
		transaction2.setAmount(300.0);
		transaction2.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
		transaction2.setSourceAccountId("acc789");
		transaction2.setDestinationAccountId(null);
		transaction2.setCreatedAt(Timestamp.valueOf("2024-09-10 13:00:00"));

		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(transaction1);
		transactionList.add(transaction2);

		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(resultSet.getString(TransactionConstants.TRANSACTION_ID_COLUMN)).thenReturn(transactionId)
				.thenReturn("txn124").thenReturn(null);
		when(resultSet.getDouble(TransactionConstants.AMOUNT_COLUMN)).thenReturn(amount).thenReturn(300.0);
		when(resultSet.getString(TransactionConstants.TRANSACTION_TYPE_COLUMN)).thenReturn(transactionType)
				.thenReturn("WITHDRAWAL");
		when(resultSet.getString(TransactionConstants.SOURCE_ACCOUNT_ID_COLUMN)).thenReturn(sourceAccountId)
				.thenReturn("acc789");
		when(resultSet.getString(TransactionConstants.DESTINATION_ACCOUNT_ID_COLUMN)).thenReturn(destinationAccountId)
				.thenReturn(null);
		when(resultSet.getTimestamp(TransactionConstants.CREATED_AT_COLUMN)).thenReturn(Timestamp.valueOf(createdAt))
				.thenReturn(Timestamp.valueOf("2024-09-10 13:00:00"));

		List<Transaction> result = transactionDAO.getAllTransactions();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(transaction1, result.get(0));
		assertEquals(transaction2, result.get(1));
	}

	@Test
	public void testGetAllTransactionsByAccount() throws SQLException, ClassNotFoundException {
		Transaction transaction1 = getTransactionObj();
		Transaction transaction2 = new Transaction();
		transaction2.setTransactionId("txn124");
		transaction2.setAmount(300.0);
		transaction2.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
		transaction2.setSourceAccountId("acc789");
		transaction2.setDestinationAccountId(null);
		transaction2.setCreatedAt(Timestamp.valueOf("2024-09-10 13:00:00"));

		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(transaction1);
		transactionList.add(transaction2);

		String accountNo = "acc123";

		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(resultSet.getString(TransactionConstants.TRANSACTION_ID_COLUMN)).thenReturn(transactionId)
				.thenReturn("txn124").thenReturn(null);
		when(resultSet.getDouble(TransactionConstants.AMOUNT_COLUMN)).thenReturn(amount).thenReturn(300.0);
		when(resultSet.getString(TransactionConstants.TRANSACTION_TYPE_COLUMN)).thenReturn(transactionType)
				.thenReturn("WITHDRAWAL");
		when(resultSet.getString(TransactionConstants.SOURCE_ACCOUNT_ID_COLUMN)).thenReturn(sourceAccountId)
				.thenReturn("acc789");
		when(resultSet.getString(TransactionConstants.DESTINATION_ACCOUNT_ID_COLUMN)).thenReturn(destinationAccountId)
				.thenReturn(null);
		when(resultSet.getTimestamp(TransactionConstants.CREATED_AT_COLUMN)).thenReturn(Timestamp.valueOf(createdAt))
				.thenReturn(Timestamp.valueOf("2024-09-10 13:00:00"));

		List<Transaction> result = transactionDAO.getAllTransactions(accountNo);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(transaction1, result.get(0));
		assertEquals(transaction2, result.get(1));
	}

	private Transaction getTransactionObj() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(transactionId);
		transaction.setAmount(amount);
		transaction.setTransactionType(Transaction.TransactionType.valueOf(transactionType));
		transaction.setSourceAccountId(sourceAccountId);
		transaction.setDestinationAccountId(destinationAccountId);
		transaction.setCreatedAt(Timestamp.valueOf(createdAt));
		return transaction;
	}
}