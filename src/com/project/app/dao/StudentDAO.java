package com.project.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.app.model.Student;
import com.project.app.util.DBUtil;

public class StudentDAO {

	public boolean addStudent(Student s) throws SQLException {
		String sql = "INSERT INTO student (id, name, age, branch) VALUES (?, ?, ?, ?)";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, s.getId());
			ps.setString(2, s.getName());
			ps.setInt(3, s.getAge());
			ps.setString(4, s.getBranch());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean studentExists(int id) throws SQLException {
		String sql = "SELECT id FROM student WHERE id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	public Student getStudentById(int id) throws SQLException {
		String sql = "SELECT * FROM student WHERE id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new Student(rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("branch"));
			}
		}
		return null;
	}

	public void viewAllStudentsWithCourses() throws SQLException {
		String sql = "SELECT s.id, s.name, s.age, s.branch, r.course_name, r.fees_paid FROM student s LEFT JOIN registration r ON s.id = r.student_id ORDER BY s.id";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();

			System.out.println("\n==========================================================================");
			System.out.println("                  ALL STUDENTS WITH COURSE DETAILS                      ");
			System.out.println("==========================================================================");
			System.out.printf("%-5s %-15s %-4s %-8s %-22s %-10s%n", "ID", "Name", "Age", "Branch", "Course", "Fees");
			System.out.println("--------------------------------------------------------------------------");

			boolean found = false;
			while (rs.next()) {
				found = true;
				String course = rs.getString("course_name");
				String fees = rs.getString("fees_paid");

				System.out.printf("%-5d %-15s %-4d %-8s %-22s %-10s%n", rs.getInt("id"), rs.getString("name"),
						rs.getInt("age"), rs.getString("branch"), course != null ? course : "Not Enrolled",
						fees != null ? fees : "-");
			}
			if (!found)
				System.out.println("No students found.");
			System.out.println("==========================================================================");
		}
	}

	public boolean updateStudent(int id, String newName, String newBranch) throws SQLException {
		String sql = "UPDATE student SET name = ?, branch = ? WHERE id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, newName);
			ps.setString(2, newBranch);
			ps.setInt(3, id);
			return ps.executeUpdate() > 0;
		}
	}

	public boolean deleteStudentWithTransaction(int id) throws SQLException {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			con.setAutoCommit(false);

			PreparedStatement ps1 = con.prepareStatement("DELETE FROM registration WHERE student_id = ?");
			ps1.setInt(1, id);
			ps1.executeUpdate();
			ps1.close();

			PreparedStatement ps2 = con.prepareStatement("DELETE FROM student WHERE id = ?");
			ps2.setInt(1, id);
			int rows = ps2.executeUpdate();
			ps2.close();

			con.commit();
			System.out.println("[Transaction Committed Successfully]");
			return rows > 0;

		} catch (SQLException e) {
			if (con != null) {
				con.rollback();
				System.out.println("[Transaction Rolled Back]");
			}
			throw e;
		} finally {
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
			}
		}
	}

	public void highPayingStudents(double minFee) throws SQLException {
		String sql = "SELECT s.id, s.name, s.branch, r.course_name, r.fees_paid FROM student s JOIN registration r ON s.id = r.student_id WHERE r.fees_paid > ? ORDER BY r.fees_paid DESC";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDouble(1, minFee);
			ResultSet rs = ps.executeQuery();

			System.out.println("\n================================================");
			System.out.println("  HIGH PAYING STUDENTS (Fees > Rs." + minFee + ")");
			System.out.println("================================================");
			System.out.printf("%-5s %-15s %-8s %-20s %-10s%n", "ID", "Name", "Branch", "Course", "Fees");
			System.out.println("------------------------------------------------");

			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.printf("%-5d %-15s %-8s %-20s %-10.2f%n", rs.getInt("id"), rs.getString("name"),
						rs.getString("branch"), rs.getString("course_name"), rs.getDouble("fees_paid"));
			}
			if (!found)
				System.out.println("No students found above Rs." + minFee);
			System.out.println("================================================");
		}
	}
}
