package es.udc.acarballal.elmas.ffmpeg.encoder;

import es.udc.acarballal.elmas.ffmpegEncoder.AbstractCodec;
import es.udc.acarballal.elmas.ffmpegEncoder.ImageLayer;

public class SnapShotEncoder extends AbstractEncoder{
	
	private static final String IMAGE_EXTENSION_PARAMETER = "ffmpeg/image/extension";
	
	private static final String IMAGE_TIME_PARAMETER = "ffmpeg/image/time";
	private static final String IMAGE_START_PARAMETER = "ffmpeg/image/start";
	private static final String IMAGE_FRAMES_PARAMETER = "ffmpeg/image/frames";

	public SnapShotEncoder(String input, String output) {
		super(input, output, getParameter(IMAGE_EXTENSION_PARAMETER));
	}

	public AbstractCodec getCodec(){
		int time = getIntParameter(IMAGE_TIME_PARAMETER);
		int start = getIntParameter(IMAGE_START_PARAMETER);
		int frames = getIntParameter(IMAGE_FRAMES_PARAMETER);
		
		AbstractCodec codec = new ImageLayer(getBasicCodec(), time, start, frames);
		
		return codec;
		
	}
}