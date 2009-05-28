package es.udc.acarballal.elmas.web.util;

import java.io.File;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;

public class VideoMimeDetection {

	private static HashMap<String, String> videoTypes;
	private static HashMap<String, String> videoExt;
	
	static{
		videoTypes = new HashMap<String, String>();
		videoExt = new HashMap<String, String>();
		videoTypes.put("video/isivideo", "IsIVideo File");
		videoTypes.put("video/mpeg", "MPEG Video");
		videoTypes.put("video/x-mpeg2", "MPEG2 Video");
		videoTypes.put("video/msvideo", "AVI Video");
		videoTypes.put("video/quicktime", "QuickTime Video");
		videoTypes.put("video/vivo", "ViVo Video");
		videoTypes.put("video/wavelet", "WaveLET Video");
		videoTypes.put("video/x-sgi-movie", "Movie Video");
		videoTypes.put("application/octet-stream", "Possible Video Type");
		
		videoExt.put(".qt", "extension1");
		videoExt.put(".mov", "extension2");
		videoExt.put(".flv", "extension3");
		videoExt.put(".mp4", "extension4");
		videoExt.put(".3gp", "extension5");
		videoExt.put(".avi", "extension6");
		videoExt.put(".mpg", "extension7");
		videoExt.put(".mpeg", "extension8");
	}
	
	public static Boolean isValidVideoFile(String videoFile){
		File f = new File(videoFile);
		String match = new MimetypesFileTypeMap().getContentType(f);
		if (videoTypes.containsKey(match)){
			return isVideo(videoFile);
		}
		return false;
		
	}
	
	private static boolean isVideo(String videoFile){
		if(!videoFile.contains(".")) return true;
		String ext = videoFile.substring(videoFile.lastIndexOf("."));
		if(videoExt.containsKey(ext)) return true;
		return false;
		
	}
	
}
