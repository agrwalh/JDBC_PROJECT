package com.project.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.app.model.Registration;
import com.project.app.util.DBUtil;

public class RegistrationDAO {

	// Check if student is already registered for the same course
	public boolean isDuplicateRegistration(int studentId, String course) throws SQLException {
		String sql = "SELECT reg_id FROM registration WHERE student_id = ? AND course_name = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, studentId);
			ps.setString(2, course);
			ResultSet rs = ps.executeQuery();
			return rs.next(); // true = duplicate exists
		}
	}

	// Register student for a course — with TRANSACTION
	// Atomic: insert must either fully succeed or rollback
	public boolean registerCourse(Registration reg) throws SQLException {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			con.setAutoCommit(false); // BEGIN TRANSACTION

			String sql = "INSERT INTO registration (student_id, course_name, fees_paid) VALUES (?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, reg.getStudentId());
			ps.setString(2, reg.getCourseName());
			ps.setDouble(3, reg.getFeesPaid());

			int rows = ps.executeUpdate();
			ps.close();

			con.commit(); // COMMIT on success
			System.out.println("[Registration Transaction Committed]");
			return rows > 0;

		} catch (SQLException e) {
			if (con != null) {
				con.rollback(); // ROLLBACK on failure
				System.out.println("[Registration Transaction Rolled Back]");
			}
			throw e;
		} finally {
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
			}
		}
	}

	// Get all registrations for a student (for search feature)
	public List<Registration> getRegistrationsByStudentId(int studentId) throws SQLException {
		List<Registration> list = new ArrayList<>();
		String sql = "SELECT * FROM registration WHERE student_id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, studentId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Registration r = new Registration();
				r.setRegId(rs.getInt("reg_id"));
				r.setStudentId(rs.getInt("student_id"));
				r.setCourseName(rs.getString("course_name"));
				r.setFeesPaid(rs.getDouble("fees_paid"));
				list.add(r);
			}
		}
		return list;
	}

	// Update fee for a specific course registration
	public boolean updateFee(int studentId, String course, double newFee) throws SQLException {
		String sql = "UPDATE registration SET fees_paid = ? WHERE student_id = ? AND course_name = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDouble(1, newFee);
			ps.setInt(2, studentId);
			ps.setString(3, course);
			return ps.executeUpdate() > 0;
		}
	}

	// Cancel registration — removes only the registration row, NOT the student
	public boolean cancelRegistration(int studentId, String course) throws SQLException {
		String sql = "DELETE FROM registration WHERE student_id = ? AND course_name = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, studentId);
			ps.setString(2, course);
			return ps.executeUpdate() > 0;
		}
	}

	// Report: course-wise student count
	public void courseWiseCount() throws SQLException {
		String sql = "SELECT course_name, COUNT(*) AS total_students " + "FROM registration " + "GROUP BY course_name "
				+ "ORDER BY total_students DESC";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();

			System.out.println("\n==========================================");
			System.out.println("       COURSE-WISE STUDENT COUNT          ");
			System.out.println("==========================================");
			System.out.printf("%-25s %-15s%n", "Course Name", "Total Students");
			System.out.println("------------------------------------------");

			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.printf("%-25s %-15d%n", rs.getString("course_name"), rs.getInt("total_students"));
			}
			if (!found)
				System.out.println("No registrations found.");
			System.out.println("==========================================");
		}
	}
}