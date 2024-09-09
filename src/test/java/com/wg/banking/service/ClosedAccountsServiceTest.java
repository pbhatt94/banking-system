package com.wg.banking.service;

import com.wg.banking.dao.ClosedAccountsDAO;
import com.wg.banking.model.ClosedAccounts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClosedAccountsServiceTest {

    @Mock
    private ClosedAccountsDAO closedAccountsDAO;

    @InjectMocks
    private ClosedAccountsService closedAccountsService;
  
    public ClosedAccounts setUpClosedAccount() {
    	ClosedAccounts closedAccount = new ClosedAccounts();
        closedAccount.setId("10001");
        closedAccount.setUsername("closedUser");
        return closedAccount;
    }

    @Test
    public void testAddClosedAccount_Success() throws SQLException, ClassNotFoundException {
        // Setup
    	ClosedAccounts closedAccount = new ClosedAccounts();
        closedAccount.setId("10001");
        closedAccount.setUsername("closedUser");
        when(closedAccountsDAO.getClosedAccount(anyString())).thenReturn(null);
        when(closedAccountsService.addClosedAccounts(closedAccount)).thenReturn(true);
        when(closedAccountsDAO.addClosedAccounts(closedAccount)).thenReturn(true);

        // Execute
        boolean result = closedAccountsService.addClosedAccounts(closedAccount);

        // Verify
        assertTrue(result);
        verify(closedAccountsDAO, times(1)).addClosedAccounts(closedAccount);
    }

    @Test
    public void testAddClosedAccount_AlreadyClosed() throws SQLException, ClassNotFoundException {
        // Setup
    	ClosedAccounts closedAccount = setUpClosedAccount();
        when(closedAccountsDAO.getClosedAccount(anyString())).thenReturn(closedAccount); // Existing closed account

        // Execute
        boolean result = closedAccountsService.addClosedAccounts(closedAccount);

        // Verify
        assertFalse(result);
        verify(closedAccountsDAO, never()).addClosedAccounts(any(ClosedAccounts.class));
    }

    @Test
    public void testAddClosedAccount_Exception() throws SQLException, ClassNotFoundException {
        // Setup
    	ClosedAccounts closedAccount = setUpClosedAccount();
        when(closedAccountsDAO.getClosedAccount(anyString())).thenThrow(SQLException.class);

        // Execute
        boolean result = closedAccountsService.addClosedAccounts(closedAccount);

        // Verify
        assertFalse(result);
        verify(closedAccountsDAO, never()).addClosedAccounts(any(ClosedAccounts.class));
    }

    @Test
    public void testGetClosedAccount_Success() throws SQLException, ClassNotFoundException {
        // Setup
    	ClosedAccounts closedAccount = setUpClosedAccount();
        when(closedAccountsDAO.getClosedAccount(anyString())).thenReturn(closedAccount);

        // Execute
        ClosedAccounts result = closedAccountsService.getClosedAccount("user1");

        // Verify
        assertNotNull(result);
        assertEquals("closedUser", result.getUsername());
    }

    @Test
    public void testGetClosedAccount_NotFound() throws SQLException, ClassNotFoundException {
        // Setup
        when(closedAccountsDAO.getClosedAccount(anyString())).thenReturn(null);

        // Execute
        ClosedAccounts result = closedAccountsService.getClosedAccount("user1");

        // Verify
        assertNull(result);
    }

    @Test
    public void testGetClosedAccount_Exception() throws SQLException, ClassNotFoundException {
        // Setup
        when(closedAccountsDAO.getClosedAccount(anyString())).thenThrow(SQLException.class);

        // Execute
        ClosedAccounts result = closedAccountsService.getClosedAccount("user1");

        // Verify
        assertNull(result);
    }

    @Test
    public void testGetAllClosedAccounts_Success() throws SQLException, ClassNotFoundException {
        // Setup
    	ClosedAccounts closedAccount = setUpClosedAccount();
        List<ClosedAccounts> closedAccounts = Collections.singletonList(closedAccount);
        when(closedAccountsDAO.getAllClosedAccounts()).thenReturn(closedAccounts);

        // Execute
        List<ClosedAccounts> result = closedAccountsService.getAllClosedAccounts();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("closedUser", result.get(0).getUsername());
    }

    @Test
    public void testGetAllClosedAccounts_Empty() throws SQLException, ClassNotFoundException {
        // Setup
        when(closedAccountsDAO.getAllClosedAccounts()).thenReturn(Collections.emptyList());

        // Execute
        List<ClosedAccounts> result = closedAccountsService.getAllClosedAccounts();

        // Verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllClosedAccounts_Exception() throws SQLException, ClassNotFoundException {
        // Setup
        when(closedAccountsDAO.getAllClosedAccounts()).thenThrow(SQLException.class);

        // Execute
        List<ClosedAccounts> result = closedAccountsService.getAllClosedAccounts();

        // Verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}