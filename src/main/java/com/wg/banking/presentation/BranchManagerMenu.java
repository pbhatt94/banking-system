package com.wg.banking.presentation;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.wg.banking.constants.NotificationConstants;
import com.wg.banking.constants.StringConstants;
import com.wg.banking.constants.TransactionConstants;
import com.wg.banking.controller.AccountController;
import com.wg.banking.controller.BranchController;
import com.wg.banking.controller.ClosedAccountsController;
import com.wg.banking.controller.IssueController;
import com.wg.banking.controller.NotificationController;
import com.wg.banking.controller.TransactionController;
import com.wg.banking.controller.UserController;
import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.AccountDetailsDAO;
import com.wg.banking.dao.BranchDAO;
import com.wg.banking.dao.ClosedAccountsDAO;
import com.wg.banking.dao.IssueDAO;
import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.TransactionDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.helper.GetUserInput;
import com.wg.banking.helper.LoggingUtil;
import com.wg.banking.helper.printer.AccountDetailsPrinter;
import com.wg.banking.helper.printer.IssuePrinter;
import com.wg.banking.helper.printer.NotificationPrinter;
import com.wg.banking.helper.printer.TransactionPrinter;
import com.wg.banking.model.AccountDetails;
import com.wg.banking.model.Branch;
import com.wg.banking.model.Issue;
import com.wg.banking.model.Notification;
import com.wg.banking.model.Transaction;
import com.wg.banking.model.User;
import com.wg.banking.service.AccountDetailsService;
import com.wg.banking.service.AccountService;
import com.wg.banking.service.BranchService;
import com.wg.banking.service.ClosedAccountsService;
import com.wg.banking.service.IssueService;
import com.wg.banking.service.NotificationService;
import com.wg.banking.service.TransactionService;
import com.wg.banking.service.UserService;

public class BranchManagerMenu {
	private static UserDAO userDAO = new UserDAO();
	private static UserService userService = new UserService(userDAO);
	private static UserController userController= new UserController(userService); 
	
	private static BranchDAO branchDAO = new BranchDAO(); 
	private static BranchService branchService = new BranchService(branchDAO);
	private static BranchController branchController= new BranchController(branchService);
	
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
	
	private static ClosedAccountsDAO closedAccountsDAO = new ClosedAccountsDAO(); 
	private static ClosedAccountsService closedAccountsService = new ClosedAccountsService(closedAccountsDAO);
	private static ClosedAccountsController closedAccountsController = new ClosedAccountsController(closedAccountsService);
	
	private static AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
	private static AccountDetailsService accountDetailsService = new AccountDetailsService(accountDetailsDAO);
	private static Logger logger = LoggingUtil.getLogger(Menu.class);
	
	private static Scanner scanner = new Scanner(System.in);
	

	public static void showBranchManagerMenu(User user) {
        int choice;

        while (true) {
            System.out.println(StringConstants.WELCOME_TO_MANAGER_PORTAL + user.getName());
            System.out.println(StringConstants.BRANCH_MANAGER_MENU);

            choice = GetUserInput.getUserChoice();

            switch (choice) {
                case 1:
                	String branchName = branchService.getBranch(user).getBranchName();
                    displayAccountDetailsManager(user, branchName);
                    break;
                case 2:
                    displayManageIssuesForManager(user);
                    break;
                case 3:
            			System.out.println(StringConstants.MANAGE_NOTIFICATION_SUB_MENU);
            			int userChoice = GetUserInput.getUserChoice();
            			
            			switch(userChoice) {
            			case 1:
            				displayNotificationsSubMenu(user);
                            break;
                        case 2:
                            notificationController.addNotification();
                            break;
                        case 3:
                            showBranchManagerMenu(user);
                            break;
            			case 4:
            				StartMenu.showStartMenu();
                            break;
            			case 5:
            				System.exit(0);
            			default:
            				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
            			}
                    break;
                case 4:
                    displayTransactionsSubMenuManager(user);
                    break;
                case 5:
                	StartMenu.showStartMenu();
                case 6:
                	System.exit(0);
                default:
                    System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
            }
        }
	}
	
	private static void displayAccountDetailsManager(User user, String branchName) {
		try {
			System.out.println(StringConstants.GET_ACCOUNT_DETAILS_SUB_MENU_MANAGER);
			int choice  = GetUserInput.getUserChoice();
			
			List<AccountDetails> accounts = new ArrayList<>();
			switch(choice) {
			case 1:
				accounts = accountDetailsService.getAllAccountDetails(branchName);
				break;
			case 2:
				System.out.println(StringConstants.GET_ACCOUNT_DETAILS_FILTER_BY_BALANCE_SUB_MENU);
				int filterChoice = GetUserInput.getUserChoice();
				accounts = accountDetailsService.getAllAccountDetails(branchName);
				switch(filterChoice) {
				case 1:
					System.out.println(StringConstants.ENTER_THE_MINIMUM_BALANCE);
					double minimumBalance = GetUserInput.getDoubleInput();
					accounts = accountDetailsService.getAllAccountDetailsByMinimumBalance(accounts, minimumBalance);
					break;
				case 2:
					System.out.println(StringConstants.ENTER_THE_MAXIMUM_BALANCE);
					double maximumBalance = GetUserInput.getDoubleInput();
					accounts = accountDetailsService.getAllAccountDetailsByMaximumBalance(accounts, maximumBalance);
					break;
				case 3:
					System.out.println(StringConstants.ENTER_THE_MINIMUM_BALANCE);
					double minBalance = GetUserInput.getDoubleInput();
					System.out.println(StringConstants.ENTER_THE_MAXIMUM_BALANCE);
					double maxBalance = GetUserInput.getDoubleInput(); 
					accounts = accountDetailsService.getAllAccountDetailsByBalanceWithinRange(accounts, minBalance, maxBalance);
					break;
				default:
					break;
				}
				break;
			case 3:
				showBranchManagerMenu(user);
			case 4:
				StartMenu.showStartMenu();
			case 5:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
			AccountDetailsPrinter.printAccountDetails(accounts);
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
			scanner.next();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void displayManageIssuesForManager(User user) {
		while(true) {
			Branch branch = branchController.getBranch(user);
			System.out.println(StringConstants.MANAGE_ISSUE_SUB_MENU);
			int choice = GetUserInput.getUserChoice();
			
			switch(choice) {
			case 1:
            	List<Issue> issues = issueController.getAllIssuesByBranch(branch);
            	IssuePrinter.printIssues(issues);
            	break;
            case 2:
            	List<Issue> allIssues = issueController.getAllIssuesByBranch(branch);
            	
            	if(allIssues == null || allIssues.size() == 0) {
					System.out.println(StringConstants.NO_ISSUES_PRESENT);
            		break;
            	} 
            	IssuePrinter.printIssues(allIssues);
            	System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
            	int index = GetUserInput.getUserChoice();
            	
            	
            	while(index <= 0 || index > allIssues.size()) {
            		System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
            		System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
                	index = GetUserInput.getUserChoice();
            	}
            	
            	Issue issueToResolve = allIssues.get(index-1);
            	issueController.resolveIssue(issueToResolve);
            	
            	break;
            case 3:
                showBranchManagerMenu(user);
                break;
			case 4:
				StartMenu.showStartMenu();
                break;
			case 5:
				System.exit(0);
			default:
                System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
		}
	}
	
	public static void displayManageNotifications(User user) {		
		while(true) {
			System.out.println(StringConstants.MANAGE_NOTIFICATION_SUB_MENU);
			int choice = GetUserInput.getUserChoice();
			
			switch(choice) {
			case 1:
				displayNotificationsSubMenu(user);
				
                notificationController.getAllNotifications();
                break;
            case 2:
                notificationController.addNotification();
                break;
            case 3:
                showBranchManagerMenu(user);
                break;
			case 4:
				StartMenu.showStartMenu();
                break;
			case 5:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
		}
	}
	
	public static void displayNotificationsSubMenu(User user) {
		System.out.println(StringConstants.GET_ALL_NOTIFICATIONS_SUB_MENU);
		int choice = GetUserInput.getUserChoice();
		
		while(choice <= 0 || choice > 3) {
			System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
			System.out.println(StringConstants.GET_ALL_NOTIFICATIONS_SUB_MENU);
			choice = GetUserInput.getUserChoice();
		}
		
		List<NotificationDetails> allNotifications = notificationController.getAllNotificationDetails(user);
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
			displayManageNotifications(user);
			break;
		default:
			System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
		}
		NotificationPrinter.printNotifications(notifications);
	}
	
	
	
	public static void displayTransactionsSubMenuManager(User user) {
		try {
			while(true) {
				Branch branch = branchController.getBranch(user);
				System.out.println(StringConstants.MANAGE_TRANSACTION_SUB_MENU);
				int choice = GetUserInput.getUserChoice();
				
				switch(choice) {
				case 1:
					TransactionPrinter.printTransactions(transactionController.getAllTransactionsByBranch(branch.getBranchId()));
					break;
				case 2:
					List<Transaction> transactions = transactionController.getAllTransactionsByBranch(branch.getBranchId());
					filterTransactionByType(transactions, user);
					break;
				case 3:
					List<Transaction> allBranchTransactions = transactionController.getAllTransactionsByBranch(branch.getBranchId());
					filterTransactionByAmount(allBranchTransactions, user);
					break;
				case 4:
					showBranchManagerMenu(user);
				case 5:
					StartMenu.showStartMenu();
				case 6:
					System.exit(0);
				default:
					System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
				}
			}
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	}
	
	public static void displayTransactionsSubMenu(User user) {
		try {
			while(true) {
				System.out.println(StringConstants.MANAGE_TRANSACTION_SUB_MENU);
				int choice = GetUserInput.getUserChoice();
				
				switch(choice) {
				case 1:
					TransactionPrinter.printTransactions(transactionController.getAllTransactions());
					break;
				case 2:
					List<Transaction> transactions = transactionController.getAllTransactions();
					filterTransactionByType(transactions, user);
					break;
				case 3:
					List<Transaction> allTransactions = transactionController.getAllTransactions();
					filterTransactionByAmount(allTransactions, user);
					break;
				case 4:
					showBranchManagerMenu(user);
				case 5:
					StartMenu.showStartMenu();
				case 6:
					System.exit(0);
				default:
                    System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
				}
			}
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
			scanner.next();
		} catch(Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	}
	
	public static void filterTransactionByType(List<Transaction> transactions, User user) {
		try {
			while(true) {
				System.out.println(StringConstants.MANAGE_TRANSACTION_FILTER_BY_TYPE_SUB_MENU);
				int choice = GetUserInput.getUserChoice();
				
				switch(choice) {
				case 1:
					List<Transaction> withdrawals = transactions.stream()
													.filter(transaction -> transaction.getTransactionType().toString().equalsIgnoreCase(TransactionConstants.WITHDRAWAL_TYPE))
													.collect(Collectors.toList());
					TransactionPrinter.printTransactions(withdrawals);
					break;
				case 2:
					List<Transaction> deposits = transactions.stream()
					.filter(transaction -> transaction.getTransactionType().toString().equalsIgnoreCase(TransactionConstants.DEPOSIT_TYPE))
					.collect(Collectors.toList());
					
					TransactionPrinter.printTransactions(deposits);
					break;
				case 3:
					List<Transaction> transfers = transactions.stream()
					.filter(transaction -> transaction.getTransactionType().toString().equalsIgnoreCase(TransactionConstants.TRANSFER_TYPE))
					.collect(Collectors.toList());
					
					TransactionPrinter.printTransactions(transfers);
					break;
				case 4:
					displayTransactionsSubMenu(user);
				case 5:
					StartMenu.showStartMenu();
				case 6:
					System.exit(0);	
				default:
                    System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
				}
			}
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
			scanner.next();
		}
	}
	
	public static void filterTransactionByAmount(List<Transaction> transactions, User user) {
		System.out.println(StringConstants.MANAGE_TRANSACTION_FILTER_BY_AMOUNT_SUB_MENU);
		int choice = GetUserInput.getUserChoice();
		List<Transaction> requiredTransactions = new ArrayList<>();
		switch(choice) {
		case 1:
			System.out.println(StringConstants.ENTER_MINIMUM_AMOUNT_TO_FILTER_BY);
			double minAmount = GetUserInput.getDoubleInput();
			requiredTransactions = transactions.stream()
											.filter(transaction -> transaction.getAmount() >= minAmount)
											.collect(Collectors.toList());
			break;
		case 2:
			System.out.println(StringConstants.ENTER_MAXIMUM_AMOUNT_TO_FILTER_BY);
			double maxAmount = GetUserInput.getDoubleInput();
			requiredTransactions = transactions.stream()
											.filter(transaction -> transaction.getAmount() <= maxAmount)
											.collect(Collectors.toList());
			break;
		case 3:
			System.out.println(StringConstants.ENTER_MINIMUM_AMOUNT_TO_FILTER_BY);
			double miniAmount = GetUserInput.getDoubleInput();
			
			System.out.println(StringConstants.ENTER_MAXIMUM_AMOUNT_TO_FILTER_BY);
			double maxiAmount = GetUserInput.getDoubleInput();

			requiredTransactions = transactions.stream()
											.filter(transaction -> transaction.getAmount() >= miniAmount && transaction.getAmount() <= maxiAmount)
											.collect(Collectors.toList());
			break;
		case 4:
			displayTransactionsSubMenu(user);
		case 5:
			StartMenu.showStartMenu();
		case 6:
			System.exit(0);	
		default:
            System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
		}
		TransactionPrinter.printTransactions(requiredTransactions);
	}
}
