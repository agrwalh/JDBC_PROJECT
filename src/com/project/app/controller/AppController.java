package com.project.app.controller;

import com.project.app.service.StudentService;

public class AppController {

	private StudentService service = new StudentService();

	public void run() {

		System.out.println("=============================================");
		System.out.println("   STUDENT COURSE REGISTRATION SYSTEM        ");
		System.out.println("=============================================");

		int choice = 0;

		do {
			printMenu();

			try {
				choice = Integer.parseInt(InputUtil.readLine("Enter choice: ").trim());
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Please enter a number between 1 and 13.");
				continue;
			}

			switch (choice) {
			case 1:
				handleAddStudent();
				break;
			case 2:
				handleRegisterCourse();
				break;
			case 3:
				service.viewAllStudentsWithCourses();
				break;
			case 4:
				handleSearchStudent();
				break;
			case 5:
				handleUpdateStudent();
				break;
			case 6:
				handleUpdateFee();
				break;
			case 7:
				handleCancelRegistration();
				break;
			case 8:
				handleDeleteStudent();
				break;
			case 9:
				handleHighPaying();
				break;
			case 10:
				service.courseWiseCount();
				break;
			case 11:
				service.showAllCourses();
				break;
			case 12:
				service.showAllBranches();
				break;
			case 13:
				System.out.println("\nGoodbye!");
				break;
			default:
				System.out.println("ERROR: Invalid choice. Enter 1 to 13.");
			}

		} while (choice != 13);
	}

	// CASE 1 — Add Student
	private void handleAddStudent() {
		System.out.println("\n--- ADD STUDENT ---");

		service.showAllBranches();

		int id = InputUtil.readInt("Enter Student ID: ");
		if (id == -1)
			return;

		if (!Validator.studentIdFree(id))
			return;

		String name = InputUtil.readNonEmptyString("Enter Name: ");
		if (name == null)
			return;

		int age = InputUtil.readPositiveInt("Enter Age: ");
		if (age == -1)
			return;

		String branch = InputUtil.readNonEmptyString("Enter Branch (from list above): ");
		if (branch == null)
			return;

		// Validate branch against branch_master
		if (!Validator.branchExists(branch))
			return;

		System.out.println(service.addStudent(id, name, age, branch));
	}

	// CASE 2 — Register for Course
	private void handleRegisterCourse() {
		System.out.println("\n--- REGISTER FOR COURSE ---");

		service.showAllCourses();

		int studentId = InputUtil.readInt("Enter Student ID: ");
		if (studentId == -1)
			return;

		if (!Validator.studentExists(studentId))
			return;

		System.out.println("Student ID " + studentId + " found. Proceeding...");

		String course = InputUtil.readNonEmptyString("Enter Course Name (exactly as shown): ");
		if (course == null)
			return;

		if (!Validator.courseExists(course))
			return;

		System.out.println("Course '" + course + "' is valid. Proceeding...");

		double fees = InputUtil.readPositiveDouble("Enter Fees Amount: ");
		if (fees == -1)
			return;

		System.out.println(service.registerCourse(studentId, course, fees));
	}

	private void handleSearchStudent() {
		System.out.println("\n--- SEARCH STUDENT ---");

		int id = InputUtil.readInt("Enter Student ID: ");
		if (id == -1)
			return;

		service.searchStudentById(id);
	}

	// CASE 5 — Update Student
	private void handleUpdateStudent() {
		System.out.println("\n--- UPDATE STUDENT ---");

		int id = InputUtil.readInt("Enter Student ID to update: ");
		if (id == -1)
			return;

		if (!Validator.studentExists(id))
			return;

		System.out.println("Student ID " + id + " found. Enter new details:");

		String newName = InputUtil.readNonEmptyString("Enter New Name: ");
		if (newName == null)
			return;

		service.showAllBranches();

		String newBranch = InputUtil.readNonEmptyString("Enter New Branch (from list above): ");
		if (newBranch == null)
			return;

		if (!Validator.branchExists(newBranch))
			return;

		System.out.println(service.updateStudent(id, newName, newBranch));
	}

	// CASE 6 — Update Course Fee
	private void handleUpdateFee() {
		System.out.println("\n--- UPDATE COURSE FEE ---");

		int studentId = InputUtil.readInt("Enter Student ID: ");
		if (studentId == -1)
			return;

		if (!Validator.studentExists(studentId))
			return;

		String course = InputUtil.readNonEmptyString("Enter Course Name: ");
		if (course == null)
			return;

		double newFee = InputUtil.readPositiveDouble("Enter New Fee Amount: ");
		if (newFee == -1)
			return;

		System.out.println(service.updateFee(studentId, course, newFee));
	}

	// CASE 7 — Cancel Registration
	private void handleCancelRegistration() {
		System.out.println("\n--- CANCEL REGISTRATION ---");

		int studentId = InputUtil.readInt("Enter Student ID: ");
		if (studentId == -1)
			return;

		if (!Validator.studentExists(studentId))
			return;

		String course = InputUtil.readNonEmptyString("Enter Course Name to cancel: ");
		if (course == null)
			return;

		System.out.println(service.cancelRegistration(studentId, course));
	}

	// CASE 8 — Delete Student
	private void handleDeleteStudent() {
		System.out.println("\n--- DELETE STUDENT ---");

		int id = InputUtil.readInt("Enter Student ID to delete: ");
		if (id == -1)
			return;

		if (!Validator.studentExists(id))
			return;

		System.out.println("WARNING: This will permanently delete Student ID " + id + " and ALL their registrations.");

		String confirm = InputUtil.readLine("Type YES to confirm: ");
		if (!confirm.equalsIgnoreCase("YES")) {
			System.out.println("Delete cancelled. Student record is safe.");
			return;
		}

		System.out.println(service.deleteStudent(id));
	}

	private void handleHighPaying() {
		System.out.println("\n--- HIGH PAYING STUDENTS REPORT ---");

		double minFee = InputUtil.readPositiveDouble("Enter minimum fee amount: ");
		if (minFee == -1)
			return;

		service.highPayingStudents(minFee);
	}

	private void printMenu() {
		System.out.println("\n=============================================");
		System.out.println("                MAIN MENU                    ");
		System.out.println("=============================================");
		System.out.println(" 1.  Add Student");
		System.out.println(" 2.  Register for Course");
		System.out.println(" 3.  View All Students with Courses");
		System.out.println(" 4.  Search Student by ID");
		System.out.println(" 5.  Update Student");
		System.out.println(" 6.  Update Course Fee");
		System.out.println(" 7.  Cancel Registration");
		System.out.println(" 8.  Delete Student");
		System.out.println(" 9.  High Paying Students Report");
		System.out.println(" 10. Course-wise Student Count");
		System.out.println("--- Master Data ---");
		System.out.println(" 11. View All Available Courses");
		System.out.println(" 12. View All Available Branches");
		System.out.println(" 13. Exit");
		System.out.println("=============================================");
	}
}
