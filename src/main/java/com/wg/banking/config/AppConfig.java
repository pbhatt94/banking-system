package com.wg.banking.config;

import com.wg.banking.controller.AccountController;
import com.wg.banking.controller.BranchController;
import com.wg.banking.controller.ClosedAccountsController;
import com.wg.banking.controller.IssueController;
import com.wg.banking.controller.LoginController;
import com.wg.banking.controller.NotificationController;
import com.wg.banking.controller.TransactionController;
import com.wg.banking.controller.UserController;
import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.AccountDetailsDAO;
import com.wg.banking.dao.BranchDAO;
import com.wg.banking.dao.ClosedAccountsDAO;
import com.wg.banking.dao.IssueDAO;
import com.wg.banking.dao.NotificationDAO;
import com.wg.banking.dao.LoginDAO;
import com.wg.banking.dao.impl.LoginDAOImpl;
import com.wg.banking.dao.impl.AccountDAOImpl;
import com.wg.banking.dao.impl.AccountDetailsDAOImpl;
import com.wg.banking.dao.impl.BranchDAOImpl;
import com.wg.banking.dao.impl.ClosedAccountsDAOImpl;
import com.wg.banking.dao.impl.IssueDAOImpl;
import com.wg.banking.dao.impl.NotificationDAOImpl;
import com.wg.banking.dao.impl.TransactionDAOImpl;
import com.wg.banking.dao.TransactionDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.dao.impl.UserDAOImpl;
import com.wg.banking.service.AccountDetailsService;
import com.wg.banking.service.AccountService;
import com.wg.banking.service.BranchService;
import com.wg.banking.service.ClosedAccountsService;
import com.wg.banking.service.IssueService;
import com.wg.banking.service.LoginService;
import com.wg.banking.service.NotificationService;
import com.wg.banking.service.TransactionService;
import com.wg.banking.service.UserService;

public class AppConfig {

	// DAOs
	private static UserDAO userDAO = new UserDAOImpl();
	private static BranchDAO branchDAO = new BranchDAOImpl();
	private static AccountDAO accountDAO = new AccountDAOImpl();
	private static TransactionDAO transactionDAO = new TransactionDAOImpl();
	private static IssueDAO issueDAO = new IssueDAOImpl();
	private static ClosedAccountsDAO closedAccountsDAO = new ClosedAccountsDAOImpl();
	private static AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAOImpl();
	private static NotificationDAO notificationDAO = new NotificationDAOImpl();
	private static LoginDAO loginDAO = new LoginDAOImpl();

	// Services
	private static UserService userService = new UserService(userDAO);
	private static BranchService branchService = new BranchService(branchDAO);
	private static AccountService accountService = new AccountService(accountDAO);
	private static TransactionService transactionService = new TransactionService(transactionDAO);
	private static IssueService issueService = new IssueService(issueDAO);
	private static ClosedAccountsService closedAccountsService = new ClosedAccountsService(closedAccountsDAO);
	private static AccountDetailsService accountDetailsService = new AccountDetailsService(accountDetailsDAO);
	private static NotificationService notificationService = new NotificationService(notificationDAO, userService,
			branchService, accountService);
	private static LoginService loginService = new LoginService(loginDAO);

	// Controllers
	private static UserController userController = new UserController(userService);
	private static BranchController branchController = new BranchController(branchService);
	private static AccountController accountController = new AccountController(accountService);
	private static TransactionController transactionController = new TransactionController(transactionService);
	private static IssueController issueController = new IssueController(issueService);
	private static ClosedAccountsController closedAccountsController = new ClosedAccountsController(
			closedAccountsService);
	private static NotificationController notificationController = new NotificationController(notificationService);
	private static LoginController loginController = new LoginController(loginService);

	// Getters for Controllers
	public static UserController getUserController() {
		return userController;
	}

	public static BranchController getBranchController() {
		return branchController;
	}

	public static AccountController getAccountController() {
		return accountController;
	}

	public static TransactionController getTransactionController() {
		return transactionController;
	}

	public static IssueController getIssueController() {
		return issueController;
	}

	public static ClosedAccountsController getClosedAccountsController() {
		return closedAccountsController;
	}

	public static NotificationController getNotificationController() {
		return notificationController;
	}

	public static LoginController getLoginController() {
		return loginController;
	}
}
