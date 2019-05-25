package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Utils {
	public static ArrayList<String> getLines(String file, boolean removeHeader) {
		ArrayList<String> line = new ArrayList<String>();
		Scanner fl = null;
		String read = new String();
		try {
			fl = new Scanner(new File(file));
			if (removeHeader) 
				read = fl.nextLine();
			while (fl.hasNextLine()) {
				read = fl.nextLine();
				line.add(read);
			}
		} catch (FileNotFoundException e ) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		}
		fl.close();
		
		return line;
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