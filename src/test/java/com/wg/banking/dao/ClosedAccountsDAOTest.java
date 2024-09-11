package com.wg.banking.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.constants.ClosedAccountsConstants;
import com.wg.banking.model.ClosedAccounts;

public class ClosedAccountsDAOTest {
    private final String id = "closed123";
    private final String username = "user123";
    private final java.sql.Timestamp closedAt = new java.sql.Timestamp(System.currentTimeMillis());

    @InjectMocks
    private ClosedAccountsDAO closedAccountsDAO;

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
    public void testAddClosedAccountsSuccess() throws SQLException, ClassNotFoundException {
        ClosedAccounts closedAccount = getClosedAccountObj();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = closedAccountsDAO.addClosedAccounts(closedAccount);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddClosedAccountsFailure() throws SQLException, ClassNotFoundException {
        ClosedAccounts closedAccount = getClosedAccountObj();

        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> closedAccountsDAO.addClosedAccounts(closedAccount));
    }

    @Test
    public void testGetClosedAccount() throws SQLException, ClassNotFoundException {
        ClosedAccounts closedAccount = getClosedAccountObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(ClosedAccountsConstants.COLUMN_ID)).thenReturn(id);
        when(resultSet.getString(ClosedAccountsConstants.COLUMN_USERNAME)).thenReturn(username);
        when(resultSet.getTimestamp(ClosedAccountsConstants.COLUMN_CLOSED_AT)).thenReturn(closedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        ClosedAccounts result = closedAccountsDAO.getClosedAccount(username);

        assertNotNull(result);
        assertEquals(closedAccount, result);
    }

    @Test
    public void testGetClosedAccountNotFound() throws SQLException, ClassNotFoundException {
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        ClosedAccounts result = closedAccountsDAO.getClosedAccount(username);

        assertEquals(null, result);
    }

    @Test
    public void testGetAllClosedAccounts() throws SQLException, ClassNotFoundException {
        ClosedAccounts closedAccount1 = getClosedAccountObj();
        closedAccount1.setClosedAt(null);
        ClosedAccounts closedAccount2 = new ClosedAccounts();
        closedAccount2.setId("closed124");
        closedAccount2.setUsername("user124");

        List<ClosedAccounts> closedAccountsList = new ArrayList<>();
        closedAccountsList.add(closedAccount1);
        closedAccountsList.add(closedAccount2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(resultSet.getString(ClosedAccountsConstants.COLUMN_ID)).thenReturn(id).thenReturn("closed124").thenReturn(null);
        when(resultSet.getString(ClosedAccountsConstants.COLUMN_USERNAME)).thenReturn(username).thenReturn("user124");

        List<ClosedAccounts> result = closedAccountsDAO.getAllClosedAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(closedAccount1, result.get(0));
        assertEquals(closedAccount2, result.get(1));
    }

    private ClosedAccounts getClosedAccountObj() {
        ClosedAccounts closedAccount = new ClosedAccounts();
        closedAccount.setId(id);
        closedAccount.setUsername(username);
        closedAccount.setClosedAt(closedAt);
        return closedAccount;
    }
}
