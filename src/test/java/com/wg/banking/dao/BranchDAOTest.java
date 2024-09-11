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

import com.wg.banking.constants.BranchConstants;
import com.wg.banking.model.Branch;

public class BranchDAOTest {

    private final String branchId = "branch1";
    private final String branchName = "Main Branch";
    private final String branchAddress = "123 Main St";
    private final String branchManagerId = "manager1";
    private final java.sql.Date createdAt = new java.sql.Date(System.currentTimeMillis());
    private final java.sql.Date updatedAt = new java.sql.Date(System.currentTimeMillis());

    @InjectMocks
    private BranchDAO branchDAO;

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
    public void testAddBranchSuccess() throws SQLException, ClassNotFoundException {
        Branch branch = getBranchObj();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = branchDAO.addBranch(branch);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddBranchFailure() throws SQLException, ClassNotFoundException {
        Branch branch = getBranchObj();

        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> branchDAO.addBranch(branch));
    }

    @Test
    public void testGetBranchById() throws SQLException, ClassNotFoundException {
        Branch branch = getBranchObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(BranchConstants.BRANCH_ID_COLUMN)).thenReturn(branchId);
        when(resultSet.getString(BranchConstants.BRANCH_NAME_COLUMN)).thenReturn(branchName);
        when(resultSet.getString(BranchConstants.BRANCH_ADDRESS_COLUMN)).thenReturn(branchAddress);
        when(resultSet.getString(BranchConstants.BRANCH_MANAGER_COLUMN)).thenReturn(branchManagerId);
        when(resultSet.getDate(BranchConstants.CREATED_AT_COLUMN)).thenReturn(createdAt);
        when(resultSet.getDate(BranchConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Branch result = branchDAO.getBranchById(branchId);

        assertNotNull(result);
        assertEquals(branch, result);
    }

    @Test
    public void testGetBranchByIdNotFound() throws SQLException, ClassNotFoundException {
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Branch result = branchDAO.getBranchById(branchId);

        assertEquals(null, result);
    }

    @Test
    public void testGetBranchByManagerId() throws SQLException, ClassNotFoundException {
        Branch branch = getBranchObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(BranchConstants.BRANCH_ID_COLUMN)).thenReturn(branchId);
        when(resultSet.getString(BranchConstants.BRANCH_NAME_COLUMN)).thenReturn(branchName);
        when(resultSet.getString(BranchConstants.BRANCH_ADDRESS_COLUMN)).thenReturn(branchAddress);
        when(resultSet.getString(BranchConstants.BRANCH_MANAGER_COLUMN)).thenReturn(branchManagerId);
        when(resultSet.getDate(BranchConstants.CREATED_AT_COLUMN)).thenReturn(createdAt);
        when(resultSet.getDate(BranchConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Branch result = branchDAO.getBranchByManagerId(branchManagerId);

        assertNotNull(result);
        assertEquals(branch, result);
    }

    @Test
    public void testDeleteBranchSuccess() throws SQLException, ClassNotFoundException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = branchDAO.deleteBranch(branchId);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteBranchFailure() throws SQLException, ClassNotFoundException {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> branchDAO.deleteBranch(branchId));
    }

    @Test
    public void testGetAllBranches() throws SQLException, ClassNotFoundException {
        Branch branch1 = getBranchObj();
        Branch branch2 = new Branch();
        branch2.setBranchId("branch2");
        branch2.setBranchName("Branch Two");
        branch2.setBranchAddress("456 Another St");
        branch2.setBranchManagerId("manager2");
        branch2.setCreatedAt(createdAt);
        branch2.setUpdatedAt(updatedAt);

        List<Branch> branchList = new ArrayList<>();
        branchList.add(branch1);
        branchList.add(branch2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(resultSet.getString(BranchConstants.BRANCH_ID_COLUMN)).thenReturn(branchId).thenReturn("branch2").thenReturn(null);
        when(resultSet.getString(BranchConstants.BRANCH_NAME_COLUMN)).thenReturn(branchName).thenReturn("Branch Two");
        when(resultSet.getString(BranchConstants.BRANCH_ADDRESS_COLUMN)).thenReturn(branchAddress).thenReturn("456 Another St");
        when(resultSet.getString(BranchConstants.BRANCH_MANAGER_COLUMN)).thenReturn(branchManagerId).thenReturn("manager2");
        when(resultSet.getDate(BranchConstants.CREATED_AT_COLUMN)).thenReturn(createdAt).thenReturn(createdAt);
        when(resultSet.getDate(BranchConstants.UPDATED_AT_COLUMN)).thenReturn(updatedAt).thenReturn(updatedAt);

        List<Branch> result = branchDAO.getAllBranches();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(branch1, result.get(0));
        assertEquals(branch2, result.get(1));
    }

    @Test
    public void testUpdateBranchSuccess() throws SQLException, ClassNotFoundException {
        Branch branch = getBranchObj();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = branchDAO.updateBranch(branch, BranchConstants.BRANCH_NAME_COLUMN);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateBranchFailure() throws SQLException, ClassNotFoundException {
        Branch branch = getBranchObj();

        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> branchDAO.updateBranch(branch, BranchConstants.BRANCH_NAME_COLUMN));
    }

    private Branch getBranchObj() {
        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setBranchName(branchName);
        branch.setBranchAddress(branchAddress);
        branch.setBranchManagerId(branchManagerId);
        branch.setCreatedAt(createdAt);
        branch.setUpdatedAt(updatedAt);
        return branch;
    }
}
