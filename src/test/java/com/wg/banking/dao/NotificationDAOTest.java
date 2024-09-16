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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wg.banking.constants.NotificationConstants;
import com.wg.banking.dao.impl.NotificationDAOImpl;
import com.wg.banking.model.Notification;

public class NotificationDAOTest {
    private final String notificationId = "notif123";
    private final String message = "Transaction Successful";
    private final String notificationType = "ACCOUNT_ACTIVITY";
    private final String receiverId = "user123";
    private final String createdAt = "2024-09-10";

    @InjectMocks
    private NotificationDAOImpl notificationDAO;
 
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
    public void testAddNotificationSuccess() throws SQLException, ClassNotFoundException {
        Notification notification = getNotificationObj();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = notificationDAO.addNotification(notification);

        assertTrue(result);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetNotificationById() throws SQLException, ClassNotFoundException {
        Notification notification = getNotificationObj();

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(NotificationConstants.NOTIFICATION_ID_COLUMN)).thenReturn(notificationId);
        when(resultSet.getString(NotificationConstants.MESSAGE_COLUMN)).thenReturn(message);
        when(resultSet.getString(NotificationConstants.NOTIFICATION_TYPE_COLUMN)).thenReturn(notificationType);
        when(resultSet.getString(NotificationConstants.RECEIVER_ID_COLUMN)).thenReturn(receiverId);
        when(resultSet.getDate(NotificationConstants.CREATED_AT_COLUMN)).thenReturn(Date.valueOf(createdAt));

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Notification result = notificationDAO.getNotificationById(notificationId);

        assertNotNull(result);
        assertEquals(notification, result);
    }

    @Test
    public void testGetAllNotifications() throws SQLException, ClassNotFoundException {
        Notification notification1 = getNotificationObj();
        Notification notification2 = new Notification();
        notification2.setNotificationId("notif124");
        notification2.setMessage("Account Updated");
        notification2.setNotificationType(Notification.NotificationType.ACCOUNT_ACTIVITY);
        notification2.setReceiverId("user456");
        notification2.setCreatedAt(Date.valueOf("2024-09-11"));

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification1);
        notificationList.add(notification2);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(resultSet.getString(NotificationConstants.NOTIFICATION_ID_COLUMN)).thenReturn(notificationId).thenReturn("notif124").thenReturn(null);
        when(resultSet.getString(NotificationConstants.MESSAGE_COLUMN)).thenReturn(message).thenReturn("Account Updated");
        when(resultSet.getString(NotificationConstants.NOTIFICATION_TYPE_COLUMN)).thenReturn(notificationType).thenReturn("ACCOUNT_ACTIVITY");
        when(resultSet.getString(NotificationConstants.RECEIVER_ID_COLUMN)).thenReturn(receiverId).thenReturn("user456");
        when(resultSet.getDate(NotificationConstants.CREATED_AT_COLUMN)).thenReturn(Date.valueOf(createdAt)).thenReturn(Date.valueOf("2024-09-11"));

        List<Notification> result = notificationDAO.getAllNotifications();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(notification1, result.get(0));
        assertEquals(notification2, result.get(1));
    }

    @Test
    public void testGetAllNotificationsByUser() throws SQLException, ClassNotFoundException {
        Notification notification1 = getNotificationObj();
        Notification notification2 = new Notification();
        notification2.setNotificationId("notif124");
        notification2.setMessage("Account Updated");
        notification2.setNotificationType(Notification.NotificationType.ACCOUNT_ACTIVITY);
        notification2.setReceiverId("user123");
        notification2.setCreatedAt(Date.valueOf("2024-09-11"));

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification1);
        notificationList.add(notification2);

        String userId = "user123";

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(resultSet.getString(NotificationConstants.NOTIFICATION_ID_COLUMN)).thenReturn(notificationId).thenReturn("notif124").thenReturn(null);
        when(resultSet.getString(NotificationConstants.MESSAGE_COLUMN)).thenReturn(message).thenReturn("Account Updated");
        when(resultSet.getString(NotificationConstants.NOTIFICATION_TYPE_COLUMN)).thenReturn(notificationType).thenReturn("ACCOUNT_ACTIVITY");
        when(resultSet.getString(NotificationConstants.RECEIVER_ID_COLUMN)).thenReturn(receiverId).thenReturn("user123");
        when(resultSet.getDate(NotificationConstants.CREATED_AT_COLUMN)).thenReturn(Date.valueOf(createdAt)).thenReturn(Date.valueOf("2024-09-11"));

        List<Notification> result = notificationDAO.getAllNotifications(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(notification1, result.get(0));
        assertEquals(notification2, result.get(1));
    }

    private Notification getNotificationObj() {
        Notification notification = new Notification();
        notification.setNotificationId(notificationId);
        notification.setMessage(message);
        notification.setNotificationType(Notification.NotificationType.valueOf(notificationType));
        notification.setReceiverId(receiverId);
        notification.setCreatedAt(Date.valueOf(createdAt));
        return notification;
    }
}
