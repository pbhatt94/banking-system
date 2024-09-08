package com.wg.banking.service;


import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.wg.banking.constants.UserConstants;
import com.wg.banking.dao.AccountDAO;
import com.wg.banking.dao.BranchDAO;
import com.wg.banking.dao.UserDAO;
import com.wg.banking.helper.LoggingUtil;
import com.wg.banking.model.Account;
import com.wg.banking.model.Branch;
import com.wg.banking.model.User;

public class BranchService { 
    private BranchDAO branchDAO;
    private UserDAO userDAO;
    private UserService userService;
    private AccountService accountService;
    private AccountDAO accountDAO;
    
    private static Logger logger = LoggingUtil.getLogger(BranchService.class);

    public BranchService(BranchDAO branchDAO) {
        this.branchDAO = branchDAO;
        userDAO = new UserDAO();
        userService = new UserService(userDAO);
        accountDAO = new AccountDAO();
        accountService = new AccountService(accountDAO);
    }
    
    public boolean addBranch(Branch branch)  { 
        try {
        	String managerId = branch.getBranchManagerId(); 
        	User user = userService.getUserById(managerId);
        	
        	if(user == null || !user.getRole().toString().equalsIgnoreCase(UserConstants.BRANCH_MANAGER)) {
        		return false;
        	}
        	
        	List<Branch> branches = getAllBranches();
        	for(var currentBranch: branches) {
        		if(currentBranch.getBranchName().equalsIgnoreCase(branch.getBranchName())) {
        			System.out.println("Branch Name already exists!");
        			return false;
        		}
        	}

			branchDAO.addBranch(branch);
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return false;
    }
    

    public Branch getBranchById(String branchId) {
        try {
			return branchDAO.getBranchById(branchId);
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return null;
    }
    
    public Branch getBranch(User user) {
    	Branch branch = null;
        try {
        	// return null if the user is not a manager
			String userRole = user.getRole().toString();
			if(!userRole.equalsIgnoreCase(UserConstants.BRANCH_MANAGER)) {
				return null;
			}
			
			branch = branchDAO.getBranch(user.getUserId());
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return branch;
    }

    public List<Branch> getAllBranches() {
        try {
			return branchDAO.getAllBranches();
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return null;
    }


    public void updateBranch(Branch branch, String columnToUpdate) {
        try {
			branchDAO.updateBranch(branch, columnToUpdate);
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
    }

    public boolean deleteBranch(String branchId) {
        try {
        	//check if the branch contains any Account
        	List<Account> accounts = accountService.getAllAccounts(branchId);
        	if(accounts != null && !accounts.isEmpty()) {
        		return false;
        	}
			branchDAO.deleteBranch(branchId);
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
        return false;
    }
}