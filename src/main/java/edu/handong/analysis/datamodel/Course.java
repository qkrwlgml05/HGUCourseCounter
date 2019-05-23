package edu.handong.analysis.datamodel;

public class Course {
	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String CourseCredit;
	private int yearTaken;
	private int semesterCourseTaken;
	
	public Course(String line) {
		String[] lines = line.split(",");
		studentId = lines[0].trim();
		yearMonthGraduated = lines[1].trim();
		firstMajor = lines[2].trim();
		secondMajor = lines[3].trim();
		courseCode = lines[4].trim();
		courseName = lines[5].trim();
		CourseCredit = lines[6].trim();
		yearTaken = Integer.parseInt(lines[7].trim());
		semesterCourseTaken = Integer.parseInt(lines[8].trim());
	}
	
	int getYearTaken () {
		return yearTaken;
	}
	
	int getSemesterCourseTaken () {
		return semesterCourseTaken;
	}
	
	String getCurseName () {
		return courseName;
	}

}
