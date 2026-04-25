package com.project.app.service;

import java.sql.SQLException;
import java.util.List;

import com.project.app.dao.BranchMasterDAO;
import com.project.app.dao.CourseMasterDAO;
import com.project.app.dao.RegistrationDAO;
import com.project.app.dao.StudentDAO;
import com.project.app.model.Registration;
import com.project.app.model.Student;

public class StudentService {

	private StudentDAO studentDAO = new StudentDAO();
	private RegistrationDAO registrationDAO = new RegistrationDAO();
	private CourseMasterDAO courseMasterDAO = new CourseMasterDAO();
	private BranchMasterDAO branchMasterDAO = new BranchMasterDAO();

	public String addStudent(int id, String name, int age, String branch) {
		if (name == null || name.trim().isEmpty())
			return "ERROR: Name cannot be empty.";
		if (age <= 0)
			return "ERROR: Age must be greater than 0.";
		if (branch == null || branch.trim().isEmpty())
			return "ERROR: Branch cannot be empty.";
		try {
			if (studentDAO.studentExists(id))
				return "ERROR: Student ID " + id + " already exists.";
			if (!branchMasterDAO.branchExists(branch.trim()))
				return "ERROR: Branch '" + branch + "' is not valid. Choose from the list shown.";
			studentDAO.addStudent(new Student(id, name.trim(), age, branch.trim().toUpperCase()));
			return "SUCCESS: Student '" + name + "' added with branch '" + branch.toUpperCase() + "'.";
		} catch (SQLException e) {
			return "DB ERROR: " + e.getMessage();
		}
	}

	public String registerCourse(int studentId, String course, double fees) {
		if (course == null || course.trim().isEmpty())
			return "ERROR: Course name cannot be empty.";
		if (fees <= 0)
			return "ERROR: Fee must be greater than 0.";
		try {
			if (!studentDAO.studentExists(studentId))
				return "ERROR: Student ID " + studentId + " not found.";
			if (!courseMasterDAO.courseExists(course.trim())) {
				System.out.println("\nAvailable courses:");
				courseMasterDAO.getAllCourses().forEach(c -> System.out.println("  >> " + c));
				return "ERROR: Course '" + course + "' does not exist. Choose from the list above.";
			}
			if (registrationDAO.isDuplicateRegistration(studentId, course.trim()))
				return "ERROR: Student ID " + studentId + " is already registered for '" + course + "'.";
			registrationDAO.registerCourse(new Registration(studentId, course.trim(), fees));
			return "SUCCESS: Student ID " + studentId + " registered for '" + course + "' | Fee: Rs." + fees;
		} catch (SQLException e) {
			return "DB ERROR: " + e.getMessage();
		}
	}

	public void viewAllStudentsWithCourses() {
		try {
			studentDAO.viewAllStudentsWithCourses();
		} catch (SQLException e) {
			System.out.println("DB ERROR: " + e.getMessage());
		}
	}

	public void searchStudentById(int id) {
		try {
			Student s = studentDAO.getStudentById(id);
			if (s == null) {
				System.out.println("ERROR: No student found with ID: " + id);
				return;
			}
			System.out.println("\n--- Student Details ---");
			System.out.println(s);
			List<Registration> list = registrationDAO.getRegistrationsByStudentId(id);
			System.out.println("\nRegistered Courses:");
			if (list.isEmpty()) {
				System.out.println("  No courses registered yet.");
			} else {
				for (Registration r : list)
					System.out.println("  >> " + r);
			}
		} catch (SQLException e) {
			System.out.println("DB ERROR: " + e.getMessage());
		}
	}

	public String updateStudent(int id, String newName, String newBranch) {
		if (newName == null || newName.trim().isEmpty())
			return "ERROR: Name cannot be empty.";
		if (newBranch == null || newBranch.trim().isEmpty())
			return "ERROR: Branch cannot be empty.";
		try {
			if (!studentDAO.studentExists(id))
				return "ERROR: Student ID " + id + " not found.";
			if (!branchMasterDAO.branchExists(newBranch.trim()))
				return "ERROR: Branch '" + newBranch + "' is not valid. Choose from the list shown.";
			studentDAO.updateStudent(id, newName.trim(), newBranch.trim().toUpperCase());
			return "SUCCESS: Student ID " + id + " updated successfully!";
		} catch (SQLException e) {
			return "DB ERROR: " + e.getMessage();
		}
	}

	public String updateFee(int studentId, String course, double newFee) {
		if (newFee <= 0)
			return "ERROR: Fee must be positive.";
		try {
			boolean done = registrationDAO.updateFee(studentId, course, newFee);
			return done ? "SUCCESS: Fee updated to Rs." + newFee : "ERROR: Registration not found.";
		} catch (SQLException e) {
			return "DB ERROR: " + e.getMessage();
		}
	}

	public String cancelRegistration(int studentId, String course) {
		if (course == null || course.trim().isEmpty())
			return "ERROR: Course name cannot be empty.";
		try {
			boolean done = registrationDAO.cancelRegistration(studentId, course);
			return done ? "SUCCESS: Registration for '" + course + "' cancelled."
					: "ERROR: No registration found for '" + course + "'.";
		} catch (SQLException e) {
			return "DB ERROR: " + e.getMessage();
		}
	}

	public String deleteStudent(int id) {
		try {
			if (!studentDAO.studentExists(id))
				return "ERROR: Student ID " + id + " not found.";
			studentDAO.deleteStudentWithTransaction(id);
			return "SUCCESS: Student ID " + id + " and all registrations permanently deleted.";
		} catch (SQLException e) {
			return "DB ERROR (Transaction Rolled Back): " + e.getMessage();
		}
	}

	public void highPayingStudents(double minFee) {
		if (minFee < 0) {
			System.out.println("ERROR: Fee cannot be negative.");
			return;
		}
		try {
			studentDAO.highPayingStudents(minFee);
		} catch (SQLException e) {
			System.out.println("DB ERROR: " + e.getMessage());
		}
	}

	public void courseWiseCount() {
		try {
			registrationDAO.courseWiseCount();
		} catch (SQLException e) {
			System.out.println("DB ERROR: " + e.getMessage());
		}
	}

	public void showAllCourses() {
		try {
			courseMasterDAO.displayAllCourses();
		} catch (SQLException e) {
			System.out.println("DB ERROR: " + e.getMessage());
		}
	}

	public void showAllBranches() {
		try {
			branchMasterDAO.displayAllBranches();
		} catch (SQLException e) {
			System.out.println("DB ERROR: " + e.getMessage());
		}
	}
}
