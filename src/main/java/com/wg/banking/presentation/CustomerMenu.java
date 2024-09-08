package com.wg.banking.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wg.banking.constants.NotificationConstants;
import com.wg.banking.constants.StringConstants;
import com.wg.banking.controller.AccountController;
import com.wg.banking.controller.IssueController;
import com.wg.banking.controller.NotificationController;
import com.wg.banking.controller.TransactionController;
import com.wg.banking.controller.UserController;
import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.IssueDAO;
import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.TransactionDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.helper.GetUserInput;
import com.wg.banking.helper.LoggingUtil;
import com.wg.banking.helper.UniqueIdGenerator;
import com.wg.banking.helper.ValidateInputs;
import com.wg.banking.helper.printer.NotificationPrinter;
import com.wg.banking.helper.printer.UserPrinter;
import com.wg.banking.model.Issue;
import com.wg.banking.model.Notification;
import com.wg.banking.model.User;
import com.wg.banking.service.AccountService;
import com.wg.banking.service.IssueService;
import com.wg.banking.service.NotificationService;
import com.wg.banking.service.TransactionService;
import com.wg.banking.service.UserService;

public class CustomerMenu {

	private static UserDAO userDAO = new UserDAO();
	private static UserService userService = new UserService(userDAO);
	private static UserController userController= new UserController(userService); 
	
	private static AccountDAO accountDAO = new AccountDAO(); 
	private static AccountService accountService = new AccountService(accountDAO);
	private static AccountController accountController= new AccountController(accountService);
	
	private static TransactionDAO transactionDAO = new TransactionDAO(); 
	private static TransactionService transactionService = new TransactionService(transactionDAO);
	private static TransactionController transactionController= new TransactionController(transactionService);
	
	private static NotificationDAO notificationDAO = new NotificationDAO(); 
	private static NotificationService notificationService = new NotificationService(notificationDAO);
	private static NotificationController notificationController= new NotificationController(notificationService);
	
	private static IssueDAO issueDAO = new IssueDAO(); 
	private static IssueService issueService = new IssueService(issueDAO);
	private static IssueController issueController = new IssueController(issueService);
	
	private static Logger logger = LoggingUtil.getLogger(Menu.class);
	private static Scanner scanner = new Scanner(System.in);
	
	public static void showCustomerMenu(User user) {
        int choice;

        while (true) {
            System.out.println(StringConstants.CUSTOMER_MENU);
            
            choice = GetUserInput.getUserChoice();

            switch (choice) {
                case 1:
                    String accountNo = accountController.getAccount(user).getAccountNo();
                    System.out.println("Account Balance = " + accountController.getAccount(accountNo).getBalance());
                    break;
                case 2:
                    showWithDrawMenu(user);
                    break;
                case 3:
                    showDepositMenu(user);
                    break;
                case 4:
                    showTransferMenu(user);
                    break;
                case 5:
                    String accountNumber = accountController.getAccount(user).getAccountNo();
                    transactionController.getAllTransactions(accountNumber);
                    break;
                case 6:
                    User userProfile = userController.getUserById(user.getUserId());
                    UserPrinter.printUsers(List.of(userProfile));
                    break;
                case 7:
                    userController.updateUser(user.getUserId()); 
                    break;
                case 8:
                	displayCustomerNotificationsSubMenu(user);
                    break;
                case 9:                    
                	Issue issue = getInputForIssue(user);
                    issueController.addIssue(issue);
                    break;
                case 10:                    
                	issueController.getAllIssues(user.getUserId());
                    break;
                case 11:
                	StartMenu.showStartMenu();
                case 12:
                	System.exit(0);
                default:
                    System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
            }
        }
	}
	
	private static void showWithDrawMenu(User user) {
		try {
			System.out.println(StringConstants.ENTER_AMOUNT_TO_WITHDRAW);
			double amount = GetUserInput.getDoubleInput();
			
			while(amount <= 0) {
				System.out.println(StringConstants.INVALID_AMOUNT);
				System.out.println(StringConstants.ENTER_AMOUNT_TO_WITHDRAW);
				amount = GetUserInput.getDoubleInput();
			}
			String sourceAccountNo = accountController.getAccount(user).getAccountNo();
			
			transactionController.withdrawMoney(amount, sourceAccountNo, null);
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
			scanner.next();
		} catch(Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	} 
	
	private static void showDepositMenu(User user) {
		try {
			System.out.println(StringConstants.ENTER_AMOUNT_TO_DEPOSIT);
			double amount = GetUserInput.getDoubleInput();	
			
			while(amount <= 0) {
				System.out.println(StringConstants.INVALID_AMOUNT);
				System.out.println(StringConstants.ENTER_AMOUNT_TO_DEPOSIT);
				amount = GetUserInput.getDoubleInput();
			}
			String destinationAccountNo = accountController.getAccount(user).getAccountNo();
			
			transactionController.depositMoney(amount, null, destinationAccountNo);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	}
	
	private static void showTransferMenu(User user) {
		try {
			System.out.println(StringConstants.ENTER_ACCOUNT_NUMBER_OF_RECEIVER);
			String destinationAccountNo = scanner.nextLine();
			while(!(ValidateInputs.isValidString(destinationAccountNo))) {
				System.out.println(StringConstants.ENTER_ACCOUNT_NUMBER_OF_RECEIVER);
				destinationAccountNo = scanner.nextLine();
			}
			System.out.println(StringConstants.ENTER_AMOUNT_TO_TRANSFER);
			double amount = GetUserInput.getDoubleInput();
			while(amount <= 0) {
				System.out.println(StringConstants.INVALID_AMOUNT);
				System.out.println(StringConstants.ENTER_AMOUNT_TO_TRANSFER);
				amount = GetUserInput.getDoubleInput();
			}
			
			String sourceAccountNo = accountController.getAccount(user).getAccountNo();
			
			transactionController.transferMoney(amount, sourceAccountNo, destinationAccountNo);
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
//			scanner.next();
		} catch(Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	}	
	
	public static void displayCustomerNotificationsSubMenu(User user) {
		System.out.println(StringConstants.GET_ALL_NOTIFICATIONS_SUB_MENU);
		int choice = GetUserInput.getUserChoice();
		
		while(choice <= 0 || choice > 3) {
			System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
			System.out.println(StringConstants.GET_ALL_NOTIFICATIONS_SUB_MENU);
			choice = GetUserInput.getUserChoice();
		}
		
		List<Notification> allNotifications = notificationController.getAllNotifications(user.getUserId());
		List<Notification> notifications = new ArrayList<>();
		switch(choice) {
		case 1:
			for(var notification: allNotifications) {
				if(notification.getNotificationType().toString().equalsIgnoreCase(NotificationConstants.SYSTEM_ALERT_TYPE)) {
					notifications.add(notification);
				}
			}
			break;
		case 2:
			for(var notification: allNotifications) {
				if(notification.getNotificationType().toString().equalsIgnoreCase(NotificationConstants.ACCOUNT_ACTIVITY_TYPE)) {
					notifications.add(notification);
				}
			}
			break;
		case 3:
			showCustomerMenu(user);
			break;
		default:
			System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
		}
		NotificationPrinter.printNotifications(notifications);
	}

	
	private static Issue getInputForIssue(User user) {
		String issueId = UniqueIdGenerator.generateUniqueId();
		System.out.println(StringConstants.ENTER_YOUR_ISSUE);
		String message = scanner.nextLine();
		while(!ValidateInputs.isValidString(message)) {
			System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
			System.out.println(StringConstants.ENTER_YOUR_ISSUE);
			message = scanner.nextLine();
		}
		String userId = user.getUserId();
		Date now = new Date();
		
		Issue issue = new Issue(issueId, message, userId, Issue.Status.PENDING, now);
		return issue;
	}
	

}
