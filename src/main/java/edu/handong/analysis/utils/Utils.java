package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
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
			System.out.println("no file.");
			System.exit(0);
		}
		
		return line;
	}
	
	public static void writeAFile (ArrayList<String> lines, String targetFileName) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(targetFileName);
			for (String line : lines)
				pw.println(line);
		}catch (FileNotFoundException e) {
			System.exit(0);
		}
	}
}