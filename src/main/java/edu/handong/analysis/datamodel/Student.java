package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>();
	private HashMap<String,Integer> semesterByYearAndSemester = new HashMap<String, Integer>();
	
	public Student(String studentId) {
		this.studentId = new String(studentId);
	}
	
	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}
	
	public HashMap<String,Integer> getSemesterByYearAndSemester(){
		int semesterNum = 1;
		for(Course course: coursesTaken) {
			String semester = course.getYearTaken()+"-"+course.getSemesterCourseTaken();
			if (!semesterByYearAndSemester.containsKey(semester)){
				semesterByYearAndSemester.put(semester, semesterNum++);
			}
		}
		return semesterByYearAndSemester;
	}
	
	public int getNumCourseInNthSementer(int semester) {
		int count = 0;
		for (String key : semesterByYearAndSemester.keySet()) {
			if (semesterByYearAndSemester.get(key) == semester) {
				int year = Integer.parseInt(key.split("-")[0].trim()), semesterCourse = Integer.parseInt(key.split("-")[1].trim());
				for (Course course : coursesTaken) {
					if (course.getYearTaken() == year && course.getSemesterCourseTaken() == semesterCourse) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	public String getStudentId () {
		return studentId;
	}
	
	public ArrayList<Course> getCoursesTaken () {
		return coursesTaken;
	}
}