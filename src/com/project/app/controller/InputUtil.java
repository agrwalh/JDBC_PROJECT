package com.project.app.controller;

import java.util.Scanner;

public class InputUtil {

	private static final Scanner sc = new Scanner(System.in);

	public static int readInt(String prompt) {
		System.out.print(prompt);
		String input = sc.nextLine().trim();
		if (input.isEmpty()) {
			System.out.println("ERROR: Input cannot be empty.");
			return -1;
		}
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: '" + input + "' is not a valid number. Enter digits only.");
			return -1;
		}
	}

	public static int readPositiveInt(String prompt) {
		int value = readInt(prompt);
		if (value == -1)
			return -1;
		if (value <= 0) {
			System.out.println("ERROR: Value must be greater than 0. You entered: " + value);
			return -1;
		}
		return value;
	}

	public static double readPositiveDouble(String prompt) {
		System.out.print(prompt);
		String input = sc.nextLine().trim();
		if (input.isEmpty()) {
			System.out.println("ERROR: Input cannot be empty.");
			return -1;
		}
		try {
			double value = Double.parseDouble(input);
			if (value <= 0) {
				System.out.println("ERROR: Amount must be greater than 0. You entered: " + value);
				return -1;
			}
			return value;
		} catch (NumberFormatException e) {
			System.out.println("ERROR: '" + input + "' is not a valid amount.");
			return -1;
		}
	}

	public static String readNonEmptyString(String prompt) {
		System.out.print(prompt);
		String input = sc.nextLine().trim();
		if (input.isEmpty()) {
			System.out.println("ERROR: This field cannot be empty.");
			return null;
		}
		return input;
	}

	public static String readLine(String prompt) {
		System.out.print(prompt);
		return sc.nextLine().trim();
	}
}
