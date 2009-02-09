package es.udc.acarballal.mencoder.util;

import java.io.File;

public class DirectoryGenerator {
	
	/**
	 * @param path to directory ended with /
	 * @return Create Directory File
	 */
	public static File create(String path){
		
		File dir = new File(path + NameGenerator.generateString(8));
		boolean created = dir.mkdir();
		while (!created){
			dir = new File(path + NameGenerator.generateString(8));
			created = dir.mkdir();
		}
		return dir;
	}

}
