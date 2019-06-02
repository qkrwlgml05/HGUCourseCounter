package edu.handong.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.geometry.euclidean.twod.Line;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	private String input = null;
	private String output = null;
	private int analysis;
	private String courseCode = null;
	private int startYear;
	private int endYear;
	private boolean help;
	
	private String courseName = null;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			
			try {
				// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
				if(args.length<2)
					throw new NotEnoughArgumentException();
			} catch (NotEnoughArgumentException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
			ArrayList<CSVRecord> lines = Utils.getLines(input, true);
			
			students = loadStudentCourseRecords(lines);
			
			// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
			Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
			
			// Generate result lines to be saved.
			ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
			
			// Write a file (named like the value of resultPath) with linesTobeSaved.
			Utils.writeAFile(linesToBeSaved, output);
		}
	}

	private boolean parseOptions(Options options, String[] args) {
		// TODO Auto-generated method stub
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			analysis = Integer.parseInt(cmd.getOptionValue("a"));
			
			if (analysis == 2) {
				courseCode = cmd.getOptionValue("c");
			}
			
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e"));
			help = cmd.hasOption("h");
		} catch (Exception e) {
			printHelp(options);
			System.out.println(e);
			return false;
		}

		return true;
	}

	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer = "";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

	private Options createOptions() {
		// TODO Auto-generated method stub		
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Analysis option")
				.required()
				.build());
		
		options.addOption(Option.builder("c").longOpt("input")
				.desc("Set an input file path.")
				.hasArg()
				.argName("Input path")
				//.required(options.getOption("a").getArgs() == 2)
				.build());
		
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g, -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.argName("Help")
				.build());
		
		return options;
	}

	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<CSVRecord> lines) {
		HashMap<String,Student> students = new HashMap<String,Student>();
		
		// TODO: Implement this method
		for (CSVRecord line : lines) {
			String key = line.get(0);//key is student id
			if (!students.containsKey(key))
				students.put(key, new Student(key));
			
			if (analysis == 2 && line.get(4).trim().equals(courseCode)) {
				courseName = line.get(5).trim();
			}
			
			students.get(key).addCourse(new Course(line));
		}
		return students; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * Year,Semester,CouseCode,CourseName,TotalStudents,StudentsTaken,Rate
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		// TODO: Implement this method
		ArrayList<String> countNumber = new ArrayList<String>();
		if (analysis == 1) {
			countNumber.add("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
			for (String key : sortedStudents.keySet()) {
				// key is studentId
				TreeMap<String, Integer> semester = new TreeMap<String, Integer>(sortedStudents.get(key).getSemesterByYearAndSemester());
				for (String key2 : semester.keySet()) { //key2 is yearTaken-semesterCourseTaken
					int year = Integer.parseInt(key2.split("-")[0].trim());
					if (year > endYear || year <= startYear) continue;
					String line = key + "," + semester.size() + "," + semester.get(key2) + "," + sortedStudents.get(key).getNumCourseInNthSementer(semester.get(key2));
					countNumber.add(line); //the line is StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
				}
			}
		} else if (analysis == 2) {
			countNumber.add("Year,Semester,CouseCode,CourseName,TotalStudents,StudentsTaken,Rate");
			
			for (int i = startYear; i <= endYear; i++) {
				for (int j = 1; j <= 4; j++) {
					String line = makeLine(sortedStudents, i, j);
					if (line != null) countNumber.add(line);
				}
			}
		}
		
		return countNumber; // do not forget to return a proper variable.
	}
	
	private String makeLine (Map<String, Student> sortedStudents, int year, int semester) {
		String line = null;
		int studentTaken = 0;
		//String courseName = null;
		ArrayList<String> courseStudents = new ArrayList<String>();
		
		for (String key : sortedStudents.keySet()) {
			for (Course course : sortedStudents.get(key).getCoursesTaken()) {
				if (course.getYearTaken() == year && course.getSemesterCourseTaken() == semester) {
					if (!courseStudents.contains(key)) {
						courseStudents.add(key);
					}
					if (course.getCourseCode().equals(courseCode)) {
						studentTaken++;
						courseName = course.getCourseName();
					}
				}
			}
		}
		if(courseStudents.size() == 0) return null;
		float rate = (float) studentTaken * 100 / courseStudents.size();
		line = year + "," + semester + "," + courseCode + "," + courseName + "," + courseStudents.size() 
		+ "," + studentTaken + "," + String.format("%.1f", rate) + "%";
		return line;
	}
}
