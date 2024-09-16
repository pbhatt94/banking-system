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
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.constants.UserConstants;
import com.wg.banking.dao.impl.UserDAOImpl;
import com.wg.banking.model.User;

public class UserDAOTest {
	private final String userId = "user1";
	private final String name = "Test User";
	private final String email = "testuser@gmail.com";
	private final String username = "testuser";
	private final String password = "testpassword";
	private final int age = 30;
	private final String gender = "M";
	private final String phoneNo = "9109192901";
	private final String address = "Delhi";
	private final String role = "CUSTOMER";
	
	@InjectMocks
	private UserDAOImpl userDAO;
 
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
	public void testAddUserSuccess() throws SQLException, ClassNotFoundException {
		User user = new User();
		
		when(preparedStatement.executeUpdate()).thenReturn(1);
 
		boolean result = userDAO.addUser(user);
 
		assertTrue(result);
		verify(preparedStatement).executeUpdate();
	}
 
	@Test
	public void testGetUserById() throws SQLException, ClassNotFoundException {
		User user = getUserObj();
		
		// Mock the ResultSet behavior
		when(resultSet.next()).thenReturn(true).thenReturn(false); // Simulate one result
		when(resultSet.getString(UserConstants.USER_ID_COLUMN)).thenReturn(userId);
		when(resultSet.getString(UserConstants.USERNAME_COLUMN)).thenReturn(username);
		when(resultSet.getString(UserConstants.NAME_COLUMN)).thenReturn(name);
		when(resultSet.getInt(UserConstants.AGE_COLUMN)).thenReturn(age);
		when(resultSet.getString(UserConstants.PASSWORD_COLUMN)).thenReturn(password);
		when(resultSet.getString(UserConstants.EMAIL_COLUMN)).thenReturn(email);
		when(resultSet.getString(UserConstants.ROLE_COLUMN)).thenReturn(role);
		when(resultSet.getString(UserConstants.PHONE_NO_COLUMN)).thenReturn(phoneNo);
		when(resultSet.getString(UserConstants.GENDER_COLUMN)).thenReturn(gender);
		when(resultSet.getString(UserConstants.ADDRESS_COLUMN)).thenReturn(address);
 
		// Mock the PreparedStatement behavior
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
		// Execute the method under test
		User result = userDAO.getUserById(userId);
 
		// Assert the results
		assertNotNull(result);
		assertEquals(user, result);
	}
 
	@Test
	public void testGetAllUsers() throws SQLException, ClassNotFoundException {
		User user1 = getUserObj();
 
		User user2 = new User();
		user2.setUserId("user2");
		user2.setName("John Doe");
		user2.setAge(30);
		user2.setUsername("username");
		user2.setPassword("password");
		user2.setEmail("john@example.com");
		user2.setRole(User.Role.CUSTOMER);
		user2.setPhoneNo("1234567890");
		user2.setGender(User.Gender.M);
		user2.setAddress("Noida");
 
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
 
		// Mock ResultSet behavior
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
 
		// Mock column values for the ResultSet
		when(resultSet.getString(UserConstants.USER_ID_COLUMN)).thenReturn(userId).thenReturn("user2").thenReturn(null);
		when(resultSet.getString(UserConstants.NAME_COLUMN)).thenReturn(name).thenReturn("John Doe")
				.thenReturn(null);
		when(resultSet.getInt(UserConstants.AGE_COLUMN)).thenReturn(30).thenReturn(30);
		when(resultSet.getString(UserConstants.USERNAME_COLUMN)).thenReturn(username).thenReturn("username");
		when(resultSet.getString(UserConstants.PASSWORD_COLUMN)).thenReturn(password).thenReturn("password");
		when(resultSet.getString(UserConstants.EMAIL_COLUMN)).thenReturn(email)
				.thenReturn("john@example.com");
		when(resultSet.getString(UserConstants.ROLE_COLUMN)).thenReturn(role).thenReturn("CUSTOMER");
		when(resultSet.getString(UserConstants.PHONE_NO_COLUMN)).thenReturn(phoneNo).thenReturn("1234567890");
		when(resultSet.getString(UserConstants.GENDER_COLUMN)).thenReturn(gender).thenReturn("M");
		when(resultSet.getString(UserConstants.ADDRESS_COLUMN)).thenReturn(address).thenReturn("Noida");
 
		List<User> result = userDAO.getAllUsers();
 
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(user1, result.get(0));
		assertEquals(user2, result.get(1));
	}
 
	@Test
	public void testUpdateUser() throws SQLException, ClassNotFoundException {
		User user = getUserObj();
 
		when(preparedStatement.executeUpdate()).thenReturn(1);
 
		boolean result = userDAO.updateUser(user, "name");
 
		assertTrue(result);
		verify(preparedStatement).executeUpdate();
	}
 
	@Test
	public void testDeleteUser() throws SQLException, ClassNotFoundException {
		when(preparedStatement.executeUpdate()).thenReturn(1);
 
		boolean result = userDAO.deleteUser(userId);
 
		assertTrue(result);
		verify(preparedStatement).executeUpdate();
	}
	
	private User getUserObj() {
		User user = new User();
		user.setUserId(userId);
		user.setName(name);
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setAge(age);
		user.setGender(User.Gender.valueOf(gender));
		user.setPhoneNo(phoneNo);
		user.setAddress(address);
		user.setRole(User.Role.valueOf(role));
		return user;
	}
}