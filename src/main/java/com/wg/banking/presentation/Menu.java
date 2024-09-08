package com.wg.banking.presentation;

import com.wg.banking.model.User;

public class Menu {
	public static void showCustomerMenu(User user) {
		CustomerMenu.showCustomerMenu(user);
	}
	
	public static void showAdminMenu(User user) {
		AdminMenu.showAdminMenu(user);
	}
	
	public static void showBranchManagerMenu(User user) {
		BranchManagerMenu.showBranchManagerMenu(user);
	}
}