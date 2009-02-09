package es.udc.acarballal.mencoder;

import java.io.File;

import es.udc.acarballal.mencoder.util.DirectoryGenerator;

public class Main {

	private static final String PATH = "C:\\carpeta_apache\\";
	
	public static void main(String[] args) {

		File dir = DirectoryGenerator.create(PATH);
		System.out.println(dir.getPath());
		FlvEncoder enc = new FlvEncoder("C:\\", 
				"\\input3.mp4", dir.getPath(), "\\output.flv");
		System.out.println(enc.execute());
		System.out.println("END.");
		
	}

}
