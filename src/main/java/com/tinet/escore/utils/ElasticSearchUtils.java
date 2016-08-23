package com.tinet.escore.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ElasticSearchUtils {
	/**
	 * json格式文件名(包含路径)
	 * 
	 * @param fullFileName
	 * @return
	 */
	public static String getJson(String fullFileName) {
		// String fullFileName =
		// "D:\\work\\MyEclipse 2015_workspace\\elasticsearch\\src\\main\\resources\\conf\\"+jsonFileName;//"E:/a.json";
		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		// System.out.println(buffer.toString());
		return buffer.toString();

	}

}
