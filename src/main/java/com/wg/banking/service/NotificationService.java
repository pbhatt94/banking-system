package com.wg.banking.service;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.helper.LoggingUtil;
import com.wg.banking.model.Notification;
import com.wg.banking.model.User;

public class NotificationService {
    private NotificationDAO notificationDAO;
    private static Logger logger = LoggingUtil.getLogger(NotificationService.class);

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public Notification getNotificationById(String notificationId) {
        try {
			return notificationDAO.getNotificationById(notificationId);
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return null;
    }

    public List<Notification> getAllNotifications() {
        try {
			List<Notification> notifications =  notificationDAO.getAllNotifications();
			List<Notification> sortedNotifications = notifications.stream()
            .sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()))
            .collect(Collectors.toList());

			return sortedNotifications;
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return null;
    }

    public void addNotification(Notification notification)  {
        try {
			notificationDAO.addNotification(notification);
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
    }
    
    public List<Notification> getAllNotifications(String userId) {
        try {
			List<Notification> notifications = notificationDAO.getAllNotifications(userId);
			return notifications;
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return null;
    }
}
