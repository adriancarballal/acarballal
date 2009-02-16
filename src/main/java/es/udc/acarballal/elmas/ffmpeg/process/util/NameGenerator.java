package es.udc.acarballal.elmas.ffmpeg.process.util;

public class NameGenerator {
	
	private static final String dictionary = 
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
		"abcdefghijklmnopqrstuvwxyz";
	
	public static String generateString(int length){
		String generateStr = new String();
		for(int i=0; i<length; i++){
			generateStr+=
				String.valueOf(dictionary.charAt((int)(Math.random()*52 )));
		}
		return generateStr;
	}

}
