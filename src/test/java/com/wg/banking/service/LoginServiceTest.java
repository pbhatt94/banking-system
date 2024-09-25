package com.wg.banking.service;

import com.wg.banking.dao.LoginDAO;
import com.wg.banking.helper.PasswordUtil;
import com.wg.banking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

public class LoginServiceTest {

	@Mock
	private LoginDAO loginDAO;

	@InjectMocks
	private LoginService loginService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test 
	public void testAuthenticateUser_Success() throws Exception {
		// Arrange
		String username = "validUser";
		String password = "validPassword";
		String hashedPassword = PasswordUtil.hashPassword(password);
		User user = new User();
		user.setUsername(username);
		user.setPassword(hashedPassword);

		when(loginDAO.authenticateUser(username, password)).thenReturn(user);

		// Act
		User result = loginService.authenticateUser(username, password);

		// Assert
		assertEquals(user, result);
		verify(loginDAO).authenticateUser(username, password);
	}

	@Test
	public void testAuthenticateUser_InvalidUsername() throws Exception {
		// Arrange
		String username = "invalidUser";
		String password = "anyPassword";

		when(loginDAO.authenticateUser(username, password)).thenReturn(null);

		// Act
		User user = loginService.authenticateUser(username, password);

		// Assert
		assertNull(user);
		verify(loginDAO).authenticateUser(username, password);
	}

	@Test
	public void testAuthenticateUser_IncorrectPassword() throws Exception {
		// Arrange
		String username = "validUser";
		String password = "wrongPassword";
		String hashedPassword = PasswordUtil.hashPassword("correctPassword");
		User user = new User();
		user.setPassword(hashedPassword);

		when(loginDAO.authenticateUser(username, password)).thenReturn(user);

		// Act
		User result = loginService.authenticateUser(username, password);

		// Assert
		assertNull(result);
		verify(loginDAO).authenticateUser(username, password);
	}

	@Test
	public void testAuthenticateUser_DaoException() throws Exception {
		// Arrange
		String username = "anyUser";
		String password = "anyPassword";

		when(loginDAO.authenticateUser(username, password)).thenThrow(new SQLException("Database error"));

		// Act
		User result = loginService.authenticateUser(username, password);

		// Assert
		assertNull(result);
		verify(loginDAO).authenticateUser(username, password);
	}
}
