package com.project.app.model;

public class CourseMaster {

	private int courseId;
	private String courseName;
	private double fees;

	public CourseMaster() {
	}

	public CourseMaster(int courseId, String courseName, double fees) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.fees = fees;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	@Override
	public String toString() {
		return "[" + courseId + "] " + courseName + " | Fee: Rs." + fees;
	}
}