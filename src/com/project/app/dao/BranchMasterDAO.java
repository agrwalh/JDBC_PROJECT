package com.project.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.app.model.BranchMaster;
import com.project.app.util.DBUtil;

public class BranchMasterDAO {

	// Check if a branch exists in branch_master
	public boolean branchExists(String branchName) throws SQLException {
		String sql = "SELECT branch_id FROM branch_master WHERE branch_name = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, branchName.trim().toUpperCase());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	// Get all branches from branch_master
	public List<BranchMaster> getAllBranches() throws SQLException {
		List<BranchMaster> list = new ArrayList<>();
		String sql = "SELECT * FROM branch_master ORDER BY branch_name";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				BranchMaster b = new BranchMaster();
				b.setBranchId(rs.getInt("branch_id"));
				b.setBranchName(rs.getString("branch_name"));
				list.add(b);
			}
		}
		return list;
	}

	// Display all branches in formatted output
	public void displayAllBranches() throws SQLException {
		String sql = "SELECT * FROM branch_master ORDER BY branch_name";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();

			System.out.println("\n==============================");
			System.out.println("   AVAILABLE BRANCHES         ");
			System.out.println("  (Only these are valid)      ");
			System.out.println("==============================");

			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.println("  [" + rs.getInt("branch_id") + "] " + rs.getString("branch_name"));
			}
			if (!found)
				System.out.println("No branches found in master.");
			System.out.println("==============================");
		}
	}
}