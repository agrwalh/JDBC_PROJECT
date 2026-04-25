package com.project.app.controller;

import java.sql.SQLException;

import com.project.app.dao.BranchMasterDAO;
import com.project.app.dao.CourseMasterDAO;
import com.project.app.dao.StudentDAO;

public class Validator {

	private static StudentDAO studentDAO = new StudentDAO();
	private static CourseMasterDAO courseMasterDAO = new CourseMasterDAO();
	private static BranchMasterDAO branchMasterDAO = new BranchMasterDAO();

	public static boolean studentExists(int id) {
		try {
			if (!studentDAO.studentExists(id)) {
				System.out.println("ERROR: Student ID " + id + " does not exist. Please add the student first.");
				return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println("DB ERROR while checking student: " + e.getMessage());
			return false;
		}
	}

	public static boolean studentIdFree(int id) {
		try {
			if (studentDAO.studentExists(id)) {
				System.out.println("ERROR: Student ID " + id + " already exists. Use a different ID.");
				return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println("DB ERROR while checking student ID: " + e.getMessage());
			return false;
		}
	}

	public static boolean courseExists(String courseName) {
		try {
			if (!courseMasterDAO.courseExists(courseName)) {
				System.out.println("ERROR: Course '" + courseName + "' does not exist in the course list.");
				System.out.println("Please choose a course exactly as shown in the list.");
				return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println("DB ERROR while checking course: " + e.getMessage());
			return false;
		}
	}

	public static boolean branchExists(String branchName) {
		try {
			if (!branchMasterDAO.branchExists(branchName)) {
				System.out.println("ERROR: Branch '" + branchName + "' is not a valid branch.");
				System.out.println("Please choose a branch from the list shown above.");
				return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println("DB ERROR while checking branch: " + e.getMessage());
			return false;
		}
	}
}
