package es.udc.acarballal.elmas.ffmpeg.process.util;

import java.io.File;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;

public class DirectoryGenerator {
	
	private static final int stringLength = 8;
	private static final String TEMPORAL_DIRECTORY_PARAMETER = "temporal/directory";
	
	/**
	 * @return Create Directory File
	 */
	public static File create(){
		
		String path;
		File dir = null;
		try {
			System.out.println("BUSCANDO...");
			path = ConfigurationParametersManager.getParameter(TEMPORAL_DIRECTORY_PARAMETER);
			System.out.println("ENCONTRADO...");
			dir = new File(path + NameGenerator.generateString(stringLength));
			System.out.println(dir.getAbsolutePath());
			boolean created = dir.mkdir();
			while (!created){
				dir = new File(path + NameGenerator.generateString(stringLength));
				created = dir.mkdir();
			}
			System.out.println("SALIMOS...");
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return dir;
	}
	
	public static int getStringLength(){
		return stringLength;
	}

}
