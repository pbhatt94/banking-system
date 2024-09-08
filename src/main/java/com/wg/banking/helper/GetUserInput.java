package com.wg.banking.helper;

import java.util.Scanner;

public class GetUserInput {
	public static int getUserChoice() {
		Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a Valid Input");
            scanner.next(); 
            System.out.print("Enter your choice: ");
        }
        int value = scanner.nextInt();
        return value;
    }
	
    public static double getDoubleInput() {
        Scanner scanner = new Scanner(System.in);
        double value = 0;
        boolean valid = false;

        while (!valid) {
            System.out.print("Enter your choice: ");
            
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                valid = true;
            } else {
                System.out.println("Invalid input. Please enter a valid Input.");
                scanner.next();
            }
        }

        return value;
    }
}