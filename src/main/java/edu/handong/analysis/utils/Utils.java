package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Utils {
	public static ArrayList<CSVRecord> getLines(String file, boolean removeHeader) {
		ArrayList<CSVRecord> lines = new ArrayList<CSVRecord>();
		try {
			Reader in = new FileReader(file);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(',').parse(in);
			int j = 0;
			
			for (CSVRecord record : records) {
				if (j == 0 && removeHeader) {
					j++;
					continue;
				}
				lines.add(record);
			}
		} catch (FileNotFoundException e ) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		return lines;
	}

	public static void writeAFile (ArrayList<String> lines, String targetFileName) {
		PrintWriter pw = null;
		try {
			File fi = new File(targetFileName);
			if (!fi.exists()) {
				fi.getParentFile().mkdirs();
			}
			pw = new PrintWriter(targetFileName);
			for (String line : lines) 
				pw.println(line);
			
			
			pw.close();
		}catch (IOException e) {
			System.out.println("directory making error");
			System.exit(0);
		}
	}
}