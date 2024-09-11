package com.wg.banking.presentation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.wg.banking.constants.AccountConstants;
import com.wg.banking.constants.NotificationConstants;
import com.wg.banking.constants.StringConstants;
import com.wg.banking.constants.TransactionConstants;
import com.wg.banking.constants.UserConstants;
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
import com.wg.banking.helper.AccountNumberGenerator;
import com.wg.banking.helper.GetUserInput;
import com.wg.banking.helper.LoggingUtil;
import com.wg.banking.helper.PasswordUtil;
import com.wg.banking.helper.UniqueIdGenerator;
import com.wg.banking.helper.ValidateInputs;
import com.wg.banking.helper.printer.AccountDetailsPrinter;
import com.wg.banking.helper.printer.BranchPrinter;
import com.wg.banking.helper.printer.IssuePrinter;
import com.wg.banking.helper.printer.NotificationDetailsPrinter;
import com.wg.banking.helper.printer.TransactionPrinter;
import com.wg.banking.helper.printer.UserPrinter;
import com.wg.banking.model.Account;
import com.wg.banking.model.AccountDetails;
import com.wg.banking.model.Branch;
import com.wg.banking.model.ClosedAccounts;
import com.wg.banking.model.Issue;
import com.wg.banking.model.Notification;
import com.wg.banking.model.NotificationDetails;
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

public class AdminMenu {

	private static UserDAO userDAO = new UserDAO();
	private static UserService userService = new UserService(userDAO);
	private static UserController userController = new UserController(userService);

	private static BranchDAO branchDAO = new BranchDAO();
	private static BranchService branchService = new BranchService(branchDAO);
	private static BranchController branchController = new BranchController(branchService);

	private static AccountDAO accountDAO = new AccountDAO();
	private static AccountService accountService = new AccountService(accountDAO);
	private static AccountController accountController = new AccountController(accountService);

	private static TransactionDAO transactionDAO = new TransactionDAO();
	private static TransactionService transactionService = new TransactionService(transactionDAO);
	private static TransactionController transactionController = new TransactionController(transactionService);

	private static IssueDAO issueDAO = new IssueDAO();
	private static IssueService issueService = new IssueService(issueDAO);
	private static IssueController issueController = new IssueController(issueService); 

	private static ClosedAccountsDAO closedAccountsDAO = new ClosedAccountsDAO();
	private static ClosedAccountsService closedAccountsService = new ClosedAccountsService(closedAccountsDAO);
	private static ClosedAccountsController closedAccountsController = new ClosedAccountsController(
			closedAccountsService);

	private static AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
	private static AccountDetailsService accountDetailsService = new AccountDetailsService(accountDetailsDAO);

	private static NotificationDAO notificationDAO = new NotificationDAO();
	private static NotificationService notificationService = new NotificationService(notificationDAO, userService,
			branchService, accountService);
	private static NotificationController notificationController = new NotificationController(notificationService);
//	private static TransactionService transactionService2 = new TransactionService(transactionDAO, accountService, notificationService); 
	private static Logger logger = LoggingUtil.getLogger(Menu.class);

	private static Scanner scanner = new Scanner(System.in); 
 
	public static void showAdminMenu(User user) {
		int choice;
		while (true) {
			System.out.println(StringConstants.ADMIN_MENU);

			choice = GetUserInput.getUserChoice();

			switch (choice) {
			case 1:
				displayManageUsers(user);
				break;
			case 2:
				displayManageAccounts(user);
				break;
			case 3:
				displayManageBranches(user);
				break;
			case 4:
				displayManageNotifications(user);
				break;
			case 5:
				displayManageIssues(user);
				break;
			case 6:
				displayTransactionsSubMenu(user);
				break;
			case 7:
				StartMenu.showStartMenu();
				break;
			case 8:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
		}
	}

	public static void displayManageUsers(User user) {
		while (true) {
			System.out.println(StringConstants.MANAGE_USER_SUB_MENU);
			int choice = GetUserInput.getUserChoice();

			switch (choice) {
			case 1:
				List<User> users = userController.getAllUsers();
				UserPrinter.printUsers(users);
				break;
			case 2:
				User newUser = null;
				do {
					newUser = getInputForNewUser();
					if (newUser != null)
						break;
					System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				} while (newUser == null);
				userController.addUser(newUser);
				if (!newUser.getRole().toString().equalsIgnoreCase(UserConstants.CUSTOMER))
					break;

				Account account = getInputForAccount(newUser);
				if (account == null) {
					System.out.println(StringConstants.FAILED_CUSTOMER_ACCOUNT_CREATION);
					break;
				}

				accountController.addAccount(account);
				System.out.println(StringConstants.SUCCESSFUL_CUSTOMER_ACCOUNT_CREATION);
				break;
			case 3:
				updateUser();
				break;
			case 4:
				List<User> inactiveUsers = userController.getAllInactiveUsers();
				UserPrinter.printUsers(inactiveUsers);
				break;
			case 5:
				showAdminMenu(user);
				break;
			case 6:
				StartMenu.showStartMenu();
				break;
			case 7:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
		}
	}

	public static void displayManageAccounts(User user) {
		while (true) {
			System.out.println(StringConstants.MANAGE_ACCOUNT_SUB_MENU);
			int choice = GetUserInput.getUserChoice();

			switch (choice) {
			case 1:
				displayAccountDetails(user);
				break;
			case 2:
				accountController.updateAccount();
				break;
			case 3:
				closeAccount();
				break;
			case 4:
				showAdminMenu(user);
				break;
			case 5:
				StartMenu.showStartMenu();
				break;
			case 6:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
		}
	}

	public static void displayManageBranches(User user) {
		while (true) {
			System.out.println(StringConstants.MANAGE_BRANCH_SUB_MENU);
			int choice = GetUserInput.getUserChoice();

			switch (choice) {
			case 1:
				System.out.println(StringConstants.AVAILABLE_BRANCHES_MESSAGE);
				List<Branch> branches = branchController.getAllBranches();
				BranchPrinter.printBranches(branches);
				break;
			case 2:
				String branchId = UniqueIdGenerator.generateUniqueId();

				System.out.print(StringConstants.ENTER_BRANCH_NAME_MESSAGE);
				String branchName = scanner.nextLine();
				while (!ValidateInputs.isValidString(branchName)) {
					System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
					System.out.print(StringConstants.ENTER_BRANCH_NAME_MESSAGE);
					branchName = scanner.nextLine();
				}

				System.out.print(StringConstants.ENTER_BRANCH_ADDRESS_MESSAGE);
				String branchAddress = scanner.nextLine();
				while (!ValidateInputs.isValidString(branchAddress)) {
					System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
					System.out.print(StringConstants.ENTER_BRANCH_ADDRESS_MESSAGE);
					branchAddress = scanner.nextLine();
				}

				List<User> managers = userController.getAvailableManagers();
				if (managers.size() == 0) {
					System.out.println(StringConstants.NO_AVAILABLE_MANAGERS_MESSAGE);
					return;
				}

				UserPrinter.printUsers(managers);
				System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
				int index = GetUserInput.getUserChoice();

				while (index <= 0 || index > managers.size()) {
					System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
					System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
					index = GetUserInput.getUserChoice();
				}

				String branchManagerId = managers.get(index - 1).getUserId();

				Date now = new Date(System.currentTimeMillis());

				Branch branch = new Branch(branchId, branchName, branchAddress, branchManagerId, now, now);
				branchController.addBranch(branch);
				break;
			case 3:
				branchController.updateBranch();
				break;
			case 4:
				branchController.deleteBranch();
				break;
			case 5:
				showAdminMenu(user);
				break;
			case 6:
				StartMenu.showStartMenu();
				break;
			case 7:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}
		}
	}

	public static void displayManageNotifications(User user) {
		while (true) {
			System.out.println(StringConstants.MANAGE_NOTIFICATION_SUB_MENU);
			int choice = GetUserInput.getUserChoice();

			switch (choice) {
			case 1:
				displayNotificationsSubMenu(user);
				break;
			case 2:
				notificationController.addNotification();
				break;
			case 3:
				showAdminMenu(user);
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

		while (choice <= 0 || choice > 3) {
			System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
			System.out.println(StringConstants.GET_ALL_NOTIFICATIONS_SUB_MENU);
			choice = GetUserInput.getUserChoice();
		}

		List<NotificationDetails> allNotifications = notificationController.getAllNotificationDetails();
		List<NotificationDetails> notifications = new ArrayList<>();
		switch (choice) {
		case 1:
			for (NotificationDetails notificationDetail : allNotifications) {
				String notificationType = notificationDetail.getNotification().getNotificationType().toString();
				if (notificationType.equalsIgnoreCase(NotificationConstants.SYSTEM_ALERT_TYPE)) {
					notifications.add(notificationDetail);
				}
			}
			break;
		case 2:
			for (NotificationDetails notificationDetail : allNotifications) {
				String notificationType = notificationDetail.getNotification().getNotificationType().toString();
				if (notificationType.equalsIgnoreCase(NotificationConstants.ACCOUNT_ACTIVITY_TYPE)) {
					notifications.add(notificationDetail);
				}
			}
			break;
		case 3:
			displayManageNotifications(user);
			break;
		default:
			System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
		}
		NotificationDetailsPrinter.printNotifications(notifications);
	}

	public static void displayManageIssues(User user) {
		while (true) {
			System.out.println(StringConstants.MANAGE_ISSUE_SUB_MENU);
			int choice = GetUserInput.getUserChoice();

			switch (choice) {
			case 1:
				List<Issue> issues = issueController.getAllIssues();
				IssuePrinter.printIssues(issues);
				break;
			case 2:
				List<Issue> allPendingIssues = issueController.getAllIssues().stream()
						.filter(issue -> issue.getStatus() == Issue.Status.PENDING).collect(Collectors.toList());
				if (allPendingIssues == null || allPendingIssues.size() == 0) {
					System.out.println(StringConstants.NO_ISSUES_PRESENT_MESSAGE);
					break;
				}
				IssuePrinter.printIssues(allPendingIssues);
				System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
				int index = GetUserInput.getUserChoice();

				while (index <= 0 || index > allPendingIssues.size()) {
					System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
					System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
					index = GetUserInput.getUserChoice();
				}

				Issue issueToResolve = allPendingIssues.get(index - 1);
				issueController.resolveIssue(issueToResolve);

				break;
			case 3:
				showAdminMenu(user);
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

	public static void displayTransactionsSubMenu(User user) {
		try {
			while (true) {
				System.out.println(StringConstants.MANAGE_TRANSACTION_SUB_MENU);
				int choice = GetUserInput.getUserChoice();

				switch (choice) {
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
					showAdminMenu(user);
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
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	}

	public static void displayAccountDetails(User user) {
		try {
			System.out.println(StringConstants.GET_ACCOUNT_DETAILS_SUB_MENU);
			int choice = GetUserInput.getUserChoice();

			List<AccountDetails> accounts = new ArrayList<>();
			switch (choice) {
			case 1:
				System.out.println(StringConstants.AVAILABLE_BRANCHES_MESSAGE);
				List<Branch> branches = branchController.getAllBranches();
				int index = 1;
				for (Branch branch : branches) {
					System.out.println(index + "     " + branch.getBranchName());
					index++;
				}
				System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
				index = GetUserInput.getUserChoice();

				while (index <= 0 || index > branches.size()) {
					System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
					System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
					index = GetUserInput.getUserChoice();
				}

				String selectedBranch = branches.get(index - 1).getBranchName();
				accounts = accountDetailsService.getAllAccountDetails(selectedBranch);
				break;
			case 2:
				accounts = accountDetailsService.getAllAccountDetails();
				System.out.println(StringConstants.GET_ACCOUNT_DETAILS_FILTER_BY_BALANCE_SUB_MENU);
				int filterChoice = GetUserInput.getUserChoice();
				switch (filterChoice) {
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
					accounts = accountDetailsService.getAllAccountDetailsByBalanceWithinRange(accounts, minBalance,
							maxBalance);
					break;
				case 4:
					displayAccountDetails(user);
				case 5:
					StartMenu.showStartMenu();
				case 6:
					System.exit(0);
				default:
					System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
				}
				break;
			case 3:
				accounts = accountDetailsService.getAllAccountDetails();
				break;
			case 4:
				displayManageAccounts(user);
			case 5:
				StartMenu.showStartMenu();
			case 6:
				System.exit(0);
			default:
				System.out.println(StringConstants.INVALID_SWITCH_CASE_INPUT);
			}

			AccountDetailsPrinter.printAccountDetails(accounts);
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
			scanner.next();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
	}

	private static User getInputForNewUser() {
		try {
			String userId = UniqueIdGenerator.generateUniqueId();

			System.out.print(StringConstants.ENTER_NAME);
			String name = scanner.nextLine();
			while (!ValidateInputs.isValidName(name)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_NAME);
				name = scanner.next();
			}

			System.out.print(StringConstants.ENTER_EMAIL);
			String email = scanner.nextLine();
			while (!ValidateInputs.isValidEmail(email)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_EMAIL);
				email = scanner.next();
			}

			System.out.print(StringConstants.ENTER_USERNAME);
			String username = scanner.nextLine();
			while (!ValidateInputs.isValidUsername(username)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_USERNAME);
				username = scanner.nextLine();
			}

			System.out.print(StringConstants.ENTER_PASSWORD);
			String password = scanner.nextLine();
			while (!PasswordUtil.isPasswordStrong(password)) {
				System.out.println(StringConstants.PASSWORD_NOT_STRONG_ENOUGH);
				System.out.print(StringConstants.ENTER_PASSWORD);
				password = scanner.next();
			}
			password = PasswordUtil.hashPassword(password);

			System.out.print(StringConstants.ENTER_AGE);
			int age = GetUserInput.getUserChoice();
			while (!ValidateInputs.isValidAge(age)) {
				System.out.println(StringConstants.ENTER_A_VALID_AGE);
				System.out.print(StringConstants.ENTER_AGE);
				age = GetUserInput.getUserChoice();
			}

			System.out.print(StringConstants.ENTER_GENDER);
			String genderString = scanner.nextLine().toUpperCase();
			while (!ValidateInputs.isValidGender(genderString)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_GENDER);
				genderString = scanner.next();
			}

			User.Gender gender = User.Gender.valueOf(genderString.toUpperCase());

			System.out.print(StringConstants.ENTER_PHONE_NUMBER);
			String phoneNo = scanner.nextLine();
			while (!ValidateInputs.isValidPhoneNo(phoneNo)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_PHONE_NUMBER);
				phoneNo = scanner.next();
			}

			System.out.print(StringConstants.ENTER_ADDRESS);
			String address = scanner.nextLine();
			while (!ValidateInputs.isValidString(address)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_ADDRESS);
				address = scanner.nextLine();
			}

			System.out.print(StringConstants.ENTER_ROLE);
			String roleString = scanner.nextLine().toUpperCase();
			while (!ValidateInputs.isValidRole(roleString)) {
				System.out.println(StringConstants.INVALID_INPUT_MESSAGE);
				System.out.print(StringConstants.ENTER_ROLE);
				roleString = scanner.next();
			}
			roleString = roleString.toUpperCase();
			User.Role role = User.Role.valueOf(roleString);
			Date now = new Date();

			User newUser = new User(userId, name, email, username, password, age, gender, phoneNo, address, role, now,
					now);

			boolean isValidUser = ValidateInputs.validateUser(newUser);

			if (!isValidUser) {
				return null;
			}

			return newUser;
		} catch (InputMismatchException e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
			scanner.next();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occurred: ", e);
			e.printStackTrace();
		}
		return null;
	}

	private static Account getInputForAccount(User user) {
		String accountNo = AccountNumberGenerator.generateAccountNumber();

		String ownerId = user.getUserId();

		System.out.println(StringConstants.AVAILABLE_BRANCHES_MESSAGE);
		List<Branch> branches = branchController.getAllBranches();
		int index = 1;
		for (Branch branch : branches) {
			System.out.println(index + "     " + branch.getBranchName());
			index++;
		}
		System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
		index = GetUserInput.getUserChoice();

		while (index <= 0 || index > branches.size()) {
			System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
			System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
			index = GetUserInput.getUserChoice();
		}

		String branchId = branches.get(index - 1).getBranchId();

		Date now = new Date(System.currentTimeMillis());

		Account account = new Account(accountNo, AccountConstants.DEFAULT_ACCOUNT_BALANCE, ownerId, branchId, now, now);
		return account;
	}

	public static void filterTransactionByType(List<Transaction> transactions, User user) {
		try {
			while (true) {
				System.out.println(StringConstants.MANAGE_TRANSACTION_FILTER_BY_TYPE_SUB_MENU);
				int choice = GetUserInput.getUserChoice();

				switch (choice) {
				case 1:
					List<Transaction> withdrawals = transactions.stream()
							.filter(transaction -> transaction.getTransactionType().toString()
									.equalsIgnoreCase(TransactionConstants.WITHDRAWAL_TYPE))
							.collect(Collectors.toList());
					TransactionPrinter.printTransactions(withdrawals);
					break;
				case 2:
					List<Transaction> deposits = transactions.stream().filter(transaction -> transaction
							.getTransactionType().toString().equalsIgnoreCase(TransactionConstants.DEPOSIT_TYPE))
							.collect(Collectors.toList());

					TransactionPrinter.printTransactions(deposits);
					break;
				case 3:
					List<Transaction> transfers = transactions.stream().filter(transaction -> transaction
							.getTransactionType().toString().equalsIgnoreCase(TransactionConstants.TRANSFER_TYPE))
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
		switch (choice) {
		case 1:
			System.out.println(StringConstants.ENTER_MINIMUM_AMOUNT_TO_FILTER_BY);
			double minAmount = GetUserInput.getDoubleInput();
			requiredTransactions = transactions.stream().filter(transaction -> transaction.getAmount() >= minAmount)
					.collect(Collectors.toList());
			break;
		case 2:
			System.out.println(StringConstants.ENTER_MAXIMUM_AMOUNT_TO_FILTER_BY);
			double maxAmount = GetUserInput.getDoubleInput();
			requiredTransactions = transactions.stream().filter(transaction -> transaction.getAmount() <= maxAmount)
					.collect(Collectors.toList());
			break;
		case 3:
			System.out.println(StringConstants.ENTER_MINIMUM_AMOUNT_TO_FILTER_BY);
			double miniAmount = GetUserInput.getDoubleInput();

			System.out.println(StringConstants.ENTER_MAXIMUM_AMOUNT_TO_FILTER_BY);
			double maxiAmount = GetUserInput.getDoubleInput();

			requiredTransactions = transactions.stream().filter(
					transaction -> transaction.getAmount() >= miniAmount && transaction.getAmount() <= maxiAmount)
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

	private static void closeAccount() {
		List<AccountDetails> accounts = accountDetailsService.getAllAccountDetails();
		AccountDetailsPrinter.printAccountDetails(accounts);
		System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
		int index = GetUserInput.getUserChoice();

		while (index <= 0 || index > accounts.size()) {
			System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
			System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
			index = GetUserInput.getUserChoice();
		}

		String accountNo = accounts.get(index - 1).getAccountNo();
		String userId = accountController.getUser(accountNo).getOwnerId();
		User user = userController.getUserById(userId);

		String closedAccountId = UniqueIdGenerator.generateUniqueId();
		String username = user.getUsername();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ClosedAccounts closedAccount = new ClosedAccounts();
		closedAccount.setId(closedAccountId);
		closedAccount.setUsername(username);
		closedAccount.setClosedAt(timestamp);
		boolean flag = closedAccountsController.addClosedAccounts(closedAccount);
		if (flag == true) {
			System.out.println(StringConstants.ACCOUNT_CLOSED_SUCCESSFULLY);
		}
	}

	private static void updateUser() {
		List<User> users = userController.getAllUsers();
		UserPrinter.printUsers(users);

		System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
		int index = GetUserInput.getUserChoice();

		while (index <= 0 || index > users.size()) {
			System.out.println(StringConstants.INVALID_INDEX_MESSAGE);
			System.out.println(StringConstants.ENTER_INDEX_MESSAGE);
			index = GetUserInput.getUserChoice();
		}

		boolean flag = userController.updateUser(users.get(index - 1).getUserId());
		if (flag == true) {
			System.out.println(StringConstants.USER_UPDATION_SUCCESS);
		} else {
			System.out.println(StringConstants.USER_UPDATION_UNSUCCESSFUL);
		}
	}
}
