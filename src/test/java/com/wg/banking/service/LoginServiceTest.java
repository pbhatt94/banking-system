package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.constants.UserConstants;
import com.wg.banking.dao.LoginDAO;
import com.wg.banking.helper.PasswordUtil;
import com.wg.banking.model.User;

public class LoginServiceTest {

//	@Mock
//	private LoginDAO loginDAO;
//
//	@Mock
//	private PasswordUtil passwordUtil;
//
//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	void testAuthenticateUserSuccess() {
//		// Arrange
//		String username = "testuser";
//		String password = "testpass";
//		String hashedPassword = "hashedtestpass";
//		Map<String, String> userMap = new HashMap<>();
//		userMap.put(UserConstants.PASSWORD_COLUMN, hashedPassword);
//
//		when(LoginDAO.authenticateUser(username, password)).thenReturn(userMap);
//		when(PasswordUtil.checkPassword(password, hashedPassword)).thenReturn(true);
//
//		// Act
//		User result = LoginService.authenticateUser(username, password);
//
//		// Assert
//		assertNotNull(result);
//	}
//
//	@Test
//	void testAuthenticateUserInvalidUsername() {
//		// Arrange
//		String username = "invaliduser";
//		String password = "password";
//		Map<String, String> userMap = new HashMap<>();
//
//		when(LoginDAO.authenticateUser(username, password)).thenReturn(userMap);
//
//		// Act
//		User result = LoginService.authenticateUser(username, password);
//
//		// Assert
//		assertNull(result);
//	}
//
//	@Test
//	void testAuthenticateUserIncorrectPassword() {
//		// Arrange
//		String username = "testuser";
//		String password = "wrongpass";
//		String hashedPassword = "hashedtestpass";
//		Map<String, String> userMap = new HashMap<>();
//		userMap.put(UserConstants.PASSWORD_COLUMN, hashedPassword);
//
//		when(LoginDAO.authenticateUser(username, password)).thenReturn(userMap);
//		when(PasswordUtil.checkPassword(password, hashedPassword)).thenReturn(false);
//
//		// Act
//		User result = LoginService.authenticateUser(username, password);
//
//		// Assert
//		assertNull(result);
//	}
//
//	@Test
//	void testAuthenticateUserEmptyMap() {
//		// Arrange
//		String username = "testuser";
//		String password = "testpass";
//		Map<String, String> userMap = new HashMap<>();
//
//		when(LoginDAO.authenticateUser(username, password)).thenReturn(userMap);
//
//		// Act
//		User result = LoginService.authenticateUser(username, password);
//
//		// Assert
//		assertNull(result);
//	}
}
