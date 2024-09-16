package com.wg.banking.presentation;

import java.util.Scanner;

import com.wg.banking.constants.StringConstants;
import com.wg.banking.controller.LoginController;

public class StartMenu {

	private static Scanner scanner = new Scanner(System.in);

	public static void showStartMenu() {
        
        while (true) {
            System.out.println(StringConstants.WELCOME_MESSAGE);
            handleSignIn();
        }
	}
	
	public static void handleSignIn() {
		System.out.println(StringConstants.ENTER_USERNAME);
        String username = scanner.nextLine();
		System.out.println(StringConstants.ENTER_PASSWORD);
        String password = scanner.nextLine();

        LoginController.authenticateUser(username,password);
	}
}
