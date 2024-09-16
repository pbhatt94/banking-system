package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.dao.BranchDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.model.Branch;
import com.wg.banking.model.User;

public class BranchServiceTest {
	@Mock 
	private BranchDAO branchDAO;

	@Mock
	private UserDAO userDAO;

	@Mock
	private UserService userService;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private BranchService branchService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddBranch_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		Branch branch = getBranchObj();

		User manager = getManagerObj();

		when(userService.getUserById("manager1")).thenReturn(manager);
		when(branchDAO.getAllBranches()).thenReturn(Arrays.asList());
		when(branchDAO.addBranch(branch)).thenReturn(true);

		// Act
		boolean result = branchService.addBranch(branch);

		// Assert
		assertTrue(result);
		verify(branchDAO).addBranch(branch);
	}

	@Test
	public void testAddBranch_Failure_ManagerNotFound() throws SQLException, ClassNotFoundException {
		// Arrange
		Branch branch = getBranchObj();

		when(userService.getUserById("manager1")).thenReturn(null);

		// Act
		boolean result = branchService.addBranch(branch);

		// Assert
		assertFalse(result);
		verify(branchDAO, never()).addBranch(any(Branch.class));
	}

	@Test
	public void testAddBranch_Failure_UserIsNotManager() throws SQLException, ClassNotFoundException {
		// Arrange
		Branch branch = getBranchObj();

		User user = getManagerObj();
		user.setRole(User.Role.CUSTOMER);

		when(userService.getUserById("user1")).thenReturn(user);

		// Act
		boolean result = branchService.addBranch(branch);

		// Assert
		assertFalse(result);
		verify(branchDAO, never()).addBranch(any(Branch.class));
	}

	@Test
	public void testAddBranch_Failure_BranchNameAlreadyExists() throws SQLException, ClassNotFoundException {
		// Arrange
		Branch branch = getBranchObj();

		User manager = getManagerObj();

		Branch existingBranch = new Branch();
		existingBranch.setBranchName(branch.getBranchName());

		when(userService.getUserById("manager1")).thenReturn(manager);
		when(branchDAO.getAllBranches()).thenReturn(Arrays.asList(existingBranch));

		// Act
		boolean result = branchService.addBranch(branch);

		// Assert
		assertFalse(result);
		verify(branchDAO, never()).addBranch(any(Branch.class));
	}

	@Test
	public void testGetBranchById_Success() throws SQLException, ClassNotFoundException {
		// Arrange
		Branch branch = getBranchObj();

		when(branchDAO.getBranchById("branch1")).thenReturn(branch);

		// Act
		Branch result = branchService.getBranchById("branch1");

		// Assert
		assertNotNull(result);
		assertEquals("branch1", result.getBranchId());
	}

	@Test
	public void testGetBranchById_Failure() throws SQLException, ClassNotFoundException {
		// Arrange
		when(branchDAO.getBranchById("branch1")).thenReturn(null);

		// Act
		Branch result = branchService.getBranchById("branch1");

		// Assert
		assertNull(result);
	}

	@Test
	public void testGetBranch_UserIsManager() throws SQLException, ClassNotFoundException {
		// Arrange
		User manager = getManagerObj();

		Branch branch = getBranchObj();

		when(branchDAO.getBranchByManagerId("manager1")).thenReturn(branch);

		// Act
		Branch result = branchService.getBranchByManagerId(manager);

		// Assert
		assertNotNull(result);
		assertEquals("branch1", result.getBranchId());
	}

	@Test
	public void testGetBranch_UserIsNotManager() {
		// Arrange
		User user = getManagerObj();
		user.setRole(User.Role.CUSTOMER);

		// Act
		Branch result = branchService.getBranchByManagerId(user);

		// Assert
		assertNull(result);
	} 

	@Test
	public void testUpdateBranch() throws SQLException, ClassNotFoundException {
		// Arrange
		Branch branch = getBranchObj();
		String columnToUpdate = "branchName";

		// Act
		branchService.updateBranch(branch, columnToUpdate);

		// Assert
		verify(branchDAO).updateBranch(branch, columnToUpdate);
	}

	private Branch getBranchObj() {
		Branch branch = new Branch();
		branch.setBranchId("branch1");
		branch.setBranchManagerId("manager1");
		branch.setBranchName("New Branch");
		branch.setBranchAddress("Delhi");
		return branch;
	}

	private User getManagerObj() {
		User manager = new User();
		manager.setUserId("manager1");
		manager.setRole(User.Role.BRANCH_MANAGER);
		return manager;
	}
}
