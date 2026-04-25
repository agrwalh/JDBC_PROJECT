package com.project.app.model;

public class BranchMaster {

	private int branchId;
	private String branchName;

	public BranchMaster() {
	}

	public BranchMaster(int branchId, String branchName) {
		this.branchId = branchId;
		this.branchName = branchName;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String b) {
		this.branchName = b;
	}

	@Override
	public String toString() {
		return "  [" + branchId + "] " + branchName;
	}
}