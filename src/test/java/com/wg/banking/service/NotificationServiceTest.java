package com.wg.banking.service;

import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.BranchDAO;
import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationDAO notificationDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserService userService;

    @Mock
    private BranchDAO branchDAO;

    @Mock
    private BranchService branchService;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testGetAllNotificationDetails() throws SQLException, ClassNotFoundException {
        // Setup
        Notification notification = new Notification();
        notification.setMessage("Unit Test notification");
        notification.setNotificationId("101");
        notification.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification.setReceiverId("user12");
        Date now = new Date();
        notification.setCreatedAt(now);
        Notification notification2 = new Notification();
        notification2.setMessage("Unit Test notification2");
        notification2.setNotificationId("102");
        notification2.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification2.setReceiverId("user12");
        notification2.setCreatedAt(now);
        List<Notification> notifications = List.of(notification, notification2);
        User user = new User();  
        user.setUserId("user12");
        List<User> users = Collections.singletonList(user);

        when(notificationDAO.getAllNotifications()).thenReturn(notifications);
        when(userService.getAllUsers()).thenReturn(users);

        // Execute
        List<NotificationDetails> result = notificationService.getAllNotificationDetails();
        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user, result.get(0).getUser());
    }

    @Test
    public void testGetAllNotificationDetailsWithManager() throws SQLException, ClassNotFoundException {
        // Setup
        Notification notification = new Notification();
        notification.setMessage("Unit Test notification");
        notification.setNotificationId("101");
        notification.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification.setReceiverId("user12");
        Date now = new Date();
        notification.setCreatedAt(now);
        List<Notification> notifications = Collections.singletonList(notification);
        User user = new User();
        user.setUserId("user12");
        List<User> users = Collections.singletonList(user);
        Branch branch = new Branch();
        branch.setBranchId("branchId1");
        Account account = new Account();
        account.setAccountNo("219301408");
        account.setBranchId("branchId1");   
        account.setOwnerId("user12");
        List<Account> accounts = Collections.singletonList(account);

        when(notificationDAO.getAllNotifications()).thenReturn(notifications);
        when(userService.getAllUsers()).thenReturn(users);
        when(branchService.getBranch(any(User.class))).thenReturn(branch);
        when(accountService.getAllAccounts(anyString())).thenReturn(accounts);

        // Execute
        List<NotificationDetails> result = notificationService.getAllNotificationDetails(user);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getUser());
    }

    @Test
    public void testGetNotificationById() throws SQLException, ClassNotFoundException {
        // Setup
    	Notification notification = new Notification();
        notification.setMessage("Unit Test notification");
        notification.setNotificationId("101");
        notification.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification.setReceiverId("user12");
        Date now = new Date();
        notification.setCreatedAt(now);

        when(notificationDAO.getNotificationById(anyString())).thenReturn(notification);

        // Execute
        Notification result = notificationService.getNotificationById("101");

        // Verify
        assertNotNull(result);
        assertEquals("101", result.getNotificationId());
    }
    
    @Test
    public void testGetAllNotifications() throws SQLException, ClassNotFoundException {
        // Setup
    	Notification notification = new Notification();
        notification.setMessage("Unit Test notification");
        notification.setNotificationId("101");
        notification.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification.setReceiverId("user12");
        Date now = new Date();
        notification.setCreatedAt(now);
        List<Notification> notifications = Collections.singletonList(notification);

        when(notificationDAO.getAllNotifications()).thenReturn(notifications);

        // Execute
        List<Notification> result = notificationService.getAllNotifications();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        String expectedNotificationId = "101"; 
		assertEquals(expectedNotificationId, result.get(0).getNotificationId());
    }

    @Test
    public void testAddNotification() throws SQLException, ClassNotFoundException {
        // Setup
    	Notification notification = new Notification();
        notification.setMessage("Unit Test notification");
        notification.setNotificationId("101");
        notification.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification.setReceiverId("user12");
        Date now = new Date();
        notification.setCreatedAt(now);

        // Execute
        notificationService.addNotification(notification);

        // Verify
        verify(notificationDAO, times(1)).addNotification(notification);
    }

    @Test
    public void testGetAllNotificationsByUserId() throws SQLException, ClassNotFoundException {
        // Setup
    	Notification notification = new Notification();
        notification.setMessage("Unit Test notification");
        notification.setNotificationId("101");
        notification.setNotificationType(Notification.NotificationType.SYSTEM_ALERT);
        notification.setReceiverId("user12");
        Date now = new Date();
        notification.setCreatedAt(now);
        List<Notification> notifications = Collections.singletonList(notification);

        when(notificationDAO.getAllNotifications(anyString())).thenReturn(notifications);

        // Execute
        List<Notification> result = notificationService.getAllNotifications("receiverId1");

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        String expectedReceiverId = "user12";
		assertEquals(expectedReceiverId, result.get(0).getReceiverId());
    }
}
