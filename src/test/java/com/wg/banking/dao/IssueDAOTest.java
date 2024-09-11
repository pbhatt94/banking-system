package com.wg.banking.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
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

import com.wg.banking.constants.IssueConstants;
import com.wg.banking.model.Issue;

public class IssueDAOTest {
    private final String issueId = "issue123";
    private final String message = "Account issue";
    private final String userId = "user123";
    private final Issue.Status status = Issue.Status.PENDING;
    private final Date createdAt = Date.valueOf("2024-09-10");

    @InjectMocks
    private IssueDAO issueDAO;

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
    public void testAddIssueSuccess() throws SQLException, ClassNotFoundException {
        Issue issue = getIssueObj();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = issueDAO.addIssue(issue);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddIssueFailure() throws SQLException, ClassNotFoundException {
        Issue issue = getIssueObj();

        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> issueDAO.addIssue(issue));
    }

    @Test
    public void testGetIssueById() throws SQLException, ClassNotFoundException {
        Issue issue = getIssueObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(IssueConstants.ISSUE_ID_COLUMN)).thenReturn(issueId);
        when(resultSet.getString(IssueConstants.MESSAGE_COLUMN)).thenReturn(message);
        when(resultSet.getString(IssueConstants.USER_ID_COLUMN)).thenReturn(userId);
        when(resultSet.getString(IssueConstants.STATUS_COLUMN)).thenReturn(status.toString());
        when(resultSet.getDate(IssueConstants.CREATED_AT_COLUMN)).thenReturn(createdAt);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Issue result = issueDAO.getIssueById(issueId);

        assertNotNull(result);
        assertEquals(issue, result);
    }

    @Test
    public void testGetIssueByIdNotFound() throws SQLException, ClassNotFoundException {
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Issue result = issueDAO.getIssueById(issueId);

        assertEquals(null, result);
    }

    @Test
    public void testGetAllIssues() throws SQLException, ClassNotFoundException {
        Issue issue1 = getIssueObj();
        Issue issue2 = new Issue();
        issue2.setIssueID("issue124");
        issue2.setMessage("Another issue");
        issue2.setUserId("user456");
        issue2.setStatus(Issue.Status.RESOLVED);
        issue2.setCreatedAt(Date.valueOf("2024-09-11"));

        List<Issue> issueList = new ArrayList<>();
        issueList.add(issue1);
        issueList.add(issue2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(resultSet.getString(IssueConstants.ISSUE_ID_COLUMN)).thenReturn(issueId).thenReturn("issue124").thenReturn(null);
        when(resultSet.getString(IssueConstants.MESSAGE_COLUMN)).thenReturn(message).thenReturn("Another issue");
        when(resultSet.getString(IssueConstants.USER_ID_COLUMN)).thenReturn(userId).thenReturn("user456");
        when(resultSet.getString(IssueConstants.STATUS_COLUMN)).thenReturn(status.toString()).thenReturn("RESOLVED");
        when(resultSet.getDate(IssueConstants.CREATED_AT_COLUMN)).thenReturn(createdAt).thenReturn(Date.valueOf("2024-09-11"));

        List<Issue> result = issueDAO.getAllIssues();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(issue1, result.get(0));
        assertEquals(issue2, result.get(1));
    }

    @Test
    public void testGetAllIssuesByUser() throws SQLException, ClassNotFoundException {
        Issue issue1 = getIssueObj();
        Issue issue2 = new Issue();
        issue2.setIssueID("issue124");
        issue2.setMessage("Another issue");
        issue2.setUserId(userId);
        issue2.setStatus(Issue.Status.RESOLVED);
        issue2.setCreatedAt(Date.valueOf("2024-09-11"));

        List<Issue> issueList = new ArrayList<>();
        issueList.add(issue1);
        issueList.add(issue2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(resultSet.getString(IssueConstants.ISSUE_ID_COLUMN)).thenReturn(issueId).thenReturn("issue124").thenReturn(null);
        when(resultSet.getString(IssueConstants.MESSAGE_COLUMN)).thenReturn(message).thenReturn("Another issue");
        when(resultSet.getString(IssueConstants.USER_ID_COLUMN)).thenReturn(userId).thenReturn(userId);
        when(resultSet.getString(IssueConstants.STATUS_COLUMN)).thenReturn(status.toString()).thenReturn("RESOLVED");
        when(resultSet.getDate(IssueConstants.CREATED_AT_COLUMN)).thenReturn(createdAt).thenReturn(Date.valueOf("2024-09-11"));

        List<Issue> result = issueDAO.getAllIssues(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(issue1, result.get(0));
        assertEquals(issue2, result.get(1));
    }

    @Test
    public void testResolveIssueSuccess() throws SQLException, ClassNotFoundException {
        Issue issue = getIssueObj();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = issueDAO.resolveIssue(issue);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testResolveIssueFailure() throws SQLException, ClassNotFoundException {
        Issue issue = getIssueObj();

        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> issueDAO.resolveIssue(issue));
    }

    private Issue getIssueObj() {
        Issue issue = new Issue();
        issue.setIssueID(issueId);
        issue.setMessage(message);
        issue.setUserId(userId);
        issue.setStatus(status);
        issue.setCreatedAt(createdAt);
        return issue;
    }
}
