package com.project.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.app.model.CourseMaster;
import com.project.app.util.DBUtil;

public class CourseMasterDAO {

	// Get all courses from course_master
	public List<CourseMaster> getAllCourses() throws SQLException {
		List<CourseMaster> list = new ArrayList<>();
		String sql = "SELECT * FROM course_master ORDER BY course_name";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CourseMaster c = new CourseMaster();
				c.setCourseId(rs.getInt("course_id"));
				c.setCourseName(rs.getString("course_name"));
				c.setFees(rs.getDouble("fees"));
				list.add(c);
			}
		}
		return list;
	}

	// Check if a course name exists in course_master
	public boolean courseExists(String courseName) throws SQLException {
		String sql = "SELECT course_id FROM course_master WHERE course_name = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, courseName);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	// Get one course by name — returns null if not found
	public CourseMaster getCourseByName(String courseName) throws SQLException {
		String sql = "SELECT * FROM course_master WHERE course_name = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, courseName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				CourseMaster c = new CourseMaster();
				c.setCourseId(rs.getInt("course_id"));
				c.setCourseName(rs.getString("course_name"));
				c.setFees(rs.getDouble("fees"));
				return c;
			}
		}
		return null;
	}

	// Display all courses in formatted table
	public void displayAllCourses() throws SQLException {
		String sql = "SELECT * FROM course_master ORDER BY course_name";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();

			System.out.println("\n===========================================");
			System.out.println("      AVAILABLE COURSES (COURSE MASTER)    ");
			System.out.println("  (Only these courses can be registered)   ");
			System.out.println("===========================================");
			System.out.printf("%-5s %-25s %-12s%n", "ID", "Course Name", "Std. Fee");
			System.out.println("-------------------------------------------");

			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.printf("%-5d %-25s Rs.%-9.0f%n", rs.getInt("course_id"), rs.getString("course_name"),
						rs.getDouble("fees"));
			}
			if (!found)
				System.out.println("No courses found in master.");
			System.out.println("===========================================");
		}
	}
}