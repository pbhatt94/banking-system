package com.wg.banking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.IssueDAO;
import com.wg.banking.model.Account;
import com.wg.banking.model.Branch;
import com.wg.banking.model.Issue;

public class IssueServiceTest {

	@Mock
	private IssueDAO issueDAO;

	@Mock
	private AccountDAO accountDAO;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private IssueService issueService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllIssuesByBranchSuccess() {
		// Arrange
		Branch branch = new Branch();
		branch.setBranchId("branch1");

		Account account = new Account();
		account.setOwnerId("user1");
		account.setBranchId("branch1");

		Issue issue = new Issue();
		issue.setUserId("user1");

		List<Account> accounts = List.of(account);
		List<Issue> issues = List.of(issue);

		when(accountService.getAllAccounts(branch.getBranchId())).thenReturn(accounts);
		when(issueService.getAllIssues()).thenReturn(issues);

		// Act
		List<Issue> result = issueService.getAllIssuesByBranch(branch);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(issue));
	}

	@Test
	void testGetAllIssuesByBranchNullBranch() {
		// Act
		List<Issue> result = issueService.getAllIssuesByBranch(null);

		// Assert
		assertNull(result);
	}

	@Test
	void testResolveIssueSuccess() throws ClassNotFoundException, SQLException {
		// Arrange
		Issue issue = new Issue();
		issue.setStatus(Issue.Status.PENDING);

		// Act
		issueService.resolveIssue(issue);

		// Assert
		verify(issueDAO).resolveIssue(issue);
		assertEquals(Issue.Status.RESOLVED, issue.getStatus());
	}

	@Test
	void testResolveIssueAlreadyResolved() throws ClassNotFoundException, SQLException {
		// Arrange
		Issue issue = new Issue();
		issue.setStatus(Issue.Status.RESOLVED);

		// Act
		issueService.resolveIssue(issue);

		// Assert
		verify(issueDAO, never()).resolveIssue(issue);
	}

	@Test
	void testGetIssueByIdSuccess() throws ClassNotFoundException, SQLException {
		// Arrange
		String issueId = "issue1";
		Issue issue = new Issue();
		when(issueDAO.getIssueById(issueId)).thenReturn(issue);

		// Act
		Issue result = issueService.getIssueById(issueId);

		// Assert
		assertNotNull(result);
		assertEquals(issue, result);
	}

	@Test
	void testGetIssueByIdException() throws ClassNotFoundException, SQLException {
		// Arrange
		String issueId = "issue1";
		when(issueDAO.getIssueById(issueId)).thenThrow(new SQLException());

		// Act
		Issue result = issueService.getIssueById(issueId);

		// Assert
		assertNull(result);
	}

	@Test
	void testGetAllIssuesSuccess() throws ClassNotFoundException, SQLException {
		// Arrange
		Issue issue = new Issue();
		List<Issue> issues = List.of(issue);
		when(issueDAO.getAllIssues()).thenReturn(issues);

		// Act
		List<Issue> result = issueService.getAllIssues();

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(issue));
	}

	@Test
	void testGetAllIssuesException() throws ClassNotFoundException, SQLException {
		// Arrange
		when(issueDAO.getAllIssues()).thenThrow(new SQLException());

		// Act
		List<Issue> result = issueService.getAllIssues();

		// Assert
		assertNull(result);
	}

	@Test
	void testGetAllIssuesByUserIdSuccess() throws ClassNotFoundException, SQLException {
		// Arrange
		String userId = "user1";
		Issue issue = new Issue();
		issue.setUserId(userId);
		List<Issue> issues = List.of(issue);
		when(issueDAO.getAllIssues()).thenReturn(issues);

		// Act
		List<Issue> result = issueService.getAllIssues(userId);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(issue));
	}

	@Test
	void testGetAllIssuesByUserIdException() throws ClassNotFoundException, SQLException {
		// Arrange
		String userId = "user1";
		when(issueDAO.getAllIssues()).thenThrow(new SQLException());

		// Act
		List<Issue> result = issueService.getAllIssues(userId);

		// Assert
		assertNull(result);
	}

	@Test
	void testAddIssueSuccess() throws ClassNotFoundException, SQLException {
		// Arrange
		Issue issue = new Issue();
		when(issueDAO.addIssue(issue)).thenReturn(true);

		// Act
		boolean result = issueService.addIssue(issue);

		// Assert
		assertTrue(result);
	}

	@Test
	void testAddIssueFailure() throws ClassNotFoundException, SQLException {
		// Arrange
		Issue issue = new Issue();
		when(issueDAO.addIssue(issue)).thenReturn(false);

		// Act
		boolean result = issueService.addIssue(issue);

		// Assert
		assertFalse(result);
	}
}
