package es.udc.acarballal.elmas.web.util;

import java.io.File;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;

public class VideoMimeDetection {

	/*
	 * Primer String - MimeType
	 * Segundo String - Video Type
	 */
	private static HashMap<String, String> videoTypes;
	
	static{
		videoTypes = new HashMap<String, String>();
		videoTypes.put("video/isivideo", "IsIVideo File");
		videoTypes.put("video/mpeg", "MPEG Video");
		videoTypes.put("video/x-mpeg2", "MPEG2 Video");
		videoTypes.put("video/msvideo", "AVI Video");
		videoTypes.put("video/quicktime", "QuickTime Video");
		videoTypes.put("video/vivo", "ViVo Video");
		videoTypes.put("video/wavelet", "WaveLET Video");
		videoTypes.put("video/x-sgi-movie", "Movie Video");
		videoTypes.put("application/octet-stream", "Possible Video Type");		
	}
	
	public static Boolean isValidVideoFile(String videoFile){
		File f = new File(videoFile);
		String match = new MimetypesFileTypeMap().getContentType(f);
		if (videoTypes.containsKey(match)){
			return true;
		}
		return false;
		
	}
	
}
