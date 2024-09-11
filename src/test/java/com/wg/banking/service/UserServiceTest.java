package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.dao.ClosedAccountsDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.model.ClosedAccounts;
import com.wg.banking.model.User;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class UserServiceTest {

	@Mock
	private UserDAO userDAO;

	@Mock
	private ClosedAccountsDAO closedAccountsDAO;

	@Mock
	private ClosedAccountsService closedAccountsService;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetUserByIdSuccess() throws SQLException, ClassNotFoundException {
		// Arrange
		String userId = "123";
		User user = new User();
		user.setUserId(userId);

		when(userDAO.getUserById(userId)).thenReturn(user);

		// Act
		User result = userService.getUserById(userId);

		// Assert
		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		verify(userDAO).getUserById(userId);
	}
 
	@Test
	void testGetUserByIdFailure() throws SQLException, ClassNotFoundException {
		// Arrange
		String userId = "123";
		when(userDAO.getUserById(userId)).thenThrow(new SQLException("Database error"));

		// Act
		User result = userService.getUserById(userId);

		// Assert
		assertNull(result);
		verify(userDAO).getUserById(userId);
	}

	@Test
	void testGetAllActiveUsers() throws SQLException, ClassNotFoundException {
		// Arrange
		User user1 = new User();
		user1.setUsername("activeUser1");
		User user2 = new User();
		user2.setUsername("inactiveUser");

		List<User> allUsers = List.of(user1, user2);

		ClosedAccounts closedAccount = new ClosedAccounts();
		closedAccount.setUsername("inactiveUser");
		List<ClosedAccounts> closedAccounts = List.of(closedAccount);

		when(userDAO.getAllUsers()).thenReturn(allUsers);
		when(closedAccountsService.getAllClosedAccounts()).thenReturn(closedAccounts);

		// Act
		List<User> activeUsers = userService.getAllActiveUsers();

		// Assert
		assertEquals(1, activeUsers.size());
		assertTrue(activeUsers.contains(user1));
		assertFalse(activeUsers.contains(user2));
		verify(userDAO).getAllUsers();
		verify(closedAccountsService).getAllClosedAccounts();
	}

	@Test
	void testAddCustomerSuccess() throws SQLException, ClassNotFoundException {
		// Arrange
		User user = new User();
		user.setRole(User.Role.CUSTOMER);

		when(userDAO.addUser(user)).thenReturn(true);

		// Act
		boolean result = userService.addUser(user);

		// Assert
		assertTrue(result);
		verify(userDAO).addUser(user);
	}

	@Test
	void testAddUserAdmin() throws ClassNotFoundException, SQLException {
		// Arrange
		User user = new User();
		user.setRole(User.Role.ADMIN);

		when(userDAO.addUser(user)).thenReturn(true);

		// Act
		boolean result = userService.addUser(user);

		// Assert
		assertFalse(result);
		verify(userDAO, never()).addUser(user);
	}

	@Test
	void testGetAllInactiveUsers() throws SQLException, ClassNotFoundException {
		// Arrange
		User user1 = new User();
		user1.setUsername("inactiveUser1");
		User user2 = new User();
		user2.setUsername("activeUser");

		List<User> allUsers = List.of(user1, user2);

		ClosedAccounts closedAccount = new ClosedAccounts();
		closedAccount.setUsername("inactiveUser1");
		List<ClosedAccounts> closedAccounts = List.of(closedAccount);

		when(userDAO.getAllUsers()).thenReturn(allUsers);
		when(closedAccountsService.getAllClosedAccounts()).thenReturn(closedAccounts);

		// Act
		List<User> inactiveUsers = userService.getAllInactiveUsers();

		// Assert
		assertEquals(1, inactiveUsers.size());
		assertTrue(inactiveUsers.contains(user1));
		assertFalse(inactiveUsers.contains(user2));
		verify(userDAO).getAllUsers();
		verify(closedAccountsService).getAllClosedAccounts();
	}

	@Test
	void testGetAllInactiveUsers_emptyList() throws SQLException, ClassNotFoundException {
		// Arrange
		User user1 = new User();
		user1.setUsername("inactiveUser1");
		User user2 = new User();
		user2.setUsername("activeUser");

		List<User> allUsers = List.of(user1, user2);

		List<ClosedAccounts> closedAccounts = Collections.emptyList();

		when(userDAO.getAllUsers()).thenReturn(allUsers);
		when(closedAccountsService.getAllClosedAccounts()).thenReturn(closedAccounts);

		// Act
		List<User> inactiveUsers = userService.getAllInactiveUsers();

		// Assert
		assertEquals(0, inactiveUsers.size());
		assertFalse(inactiveUsers.contains(user1));
		assertFalse(inactiveUsers.contains(user2));
		verify(userDAO).getAllUsers();
		verify(closedAccountsService).getAllClosedAccounts();
	}

	@Test
	void testUpdateUserFailure() throws SQLException, ClassNotFoundException {
		// Arrange
		User user = new User();
		String columnToUpdate = "role";

		when(userDAO.updateUser(user, columnToUpdate)).thenReturn(false);

		// Act
		boolean result = userService.updateUser(user, columnToUpdate);

		// Assert
		assertFalse(result);
		verify(userDAO, never()).updateUser(user, columnToUpdate);
	}

	@Test
	void testDeleteUserFailure() throws SQLException, ClassNotFoundException {
		// Arrange
		String userId = "123";
		User user = new User();
		user.setUserId(userId);
		user.setRole(User.Role.ADMIN);

		when(userDAO.getUserById(userId)).thenReturn(user);
		when(userDAO.deleteUser(userId)).thenReturn(false);

		// Act
		boolean result = userService.deleteUser(userId);

		// Assert
		assertFalse(result);
		verify(userDAO).getUserById(userId);
		verify(userDAO, never()).deleteUser(userId);
	}

	@Test
	void testDeleteUserSuccess() throws SQLException, ClassNotFoundException {
		// Arrange
		String userId = "123";
		User user = new User();
		user.setUserId(userId);
		user.setRole(User.Role.CUSTOMER);

		when(userDAO.getUserById(userId)).thenReturn(user);
		when(userDAO.deleteUser(userId)).thenReturn(true);

		// Act
		boolean result = userService.deleteUser(userId);

		// Assert
		assertTrue(result);
		verify(userDAO).getUserById(userId);
		verify(userDAO).deleteUser(userId);
	}

	@Test
	void testDeleteUserNotFound() throws SQLException, ClassNotFoundException {
		// Arrange
		String userId = "123";

		when(userDAO.getUserById(userId)).thenReturn(null);

		// Act
		boolean result = userService.deleteUser(userId);

		// Assert
		assertFalse(result);
		verify(userDAO).getUserById(userId);
		verify(userDAO, never()).deleteUser(anyString());
	}

	@Test
	void testDeleteUserAdmin() throws SQLException, ClassNotFoundException {
		// Arrange
		String userId = "123";
		User user = new User();
		user.setUserId(userId);
		user.setRole(User.Role.ADMIN);

		when(userDAO.getUserById(userId)).thenReturn(user);

		// Act
		boolean result = userService.deleteUser(userId);

		// Assert
		assertFalse(result);
		verify(userDAO).getUserById(userId);
		verify(userDAO, never()).deleteUser(userId);
	}

	@Test
	void testGetAvailableUsers() throws SQLException, ClassNotFoundException {
		// Arrange
		User user = new User();
		List<User> users = List.of(user);

		when(userDAO.getAvailableUsers()).thenReturn(users);

		// Act
		List<User> result = userService.getAvailableUsers();

		// Assert
		assertEquals(users, result);
		verify(userDAO).getAvailableUsers();
	}

	@Test
	void testGetAvailableUsers_emptyList() throws SQLException, ClassNotFoundException {
		// Arrange
		List<User> users = Collections.emptyList();

		when(userDAO.getAvailableUsers()).thenReturn(users);

		// Act
		List<User> result = userService.getAvailableUsers();

		// Assert
		assertEquals(0, result.size());
		assertEquals(users, result);
		verify(userDAO).getAvailableUsers();
	}

	@Test
	void testGetAvailableManagers() throws SQLException, ClassNotFoundException {
		// Arrange
		User manager = new User();
		manager.setRole(User.Role.BRANCH_MANAGER);
		List<User> managers = List.of(manager);

		when(userDAO.getAvailableManagers()).thenReturn(managers);

		// Act
		List<User> result = userService.getAvailableManagers();

		// Assert
		assertEquals(managers, result);
		assertEquals(1, result.size());
		verify(userDAO).getAvailableManagers();
	}
}
