package com.project.app.controller;

import java.sql.SQLException;

import com.project.app.dao.BranchMasterDAO;
import com.project.app.dao.CourseMasterDAO;
import com.project.app.dao.StudentDAO;

// All DB validation checks in one place
// Called from AppController before collecting the next input
// Returns false + prints error if check fails
// Caller does: if (!Validator.checkXxx()) return;
public class Validator {

	private static StudentDAO studentDAO = new StudentDAO();
	private static CourseMasterDAO courseMasterDAO = new CourseMasterDAO();
	private static BranchMasterDAO branchMasterDAO = new BranchMasterDAO();

	// Check student EXISTS — used before register, update, cancel, delete
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

	// Check student ID is FREE — used before adding a new student
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

	// Check course EXISTS in course_master — used before registering
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

	// Check branch EXISTS in branch_master — used before adding/updating student
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