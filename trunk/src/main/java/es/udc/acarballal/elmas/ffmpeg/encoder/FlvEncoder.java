package es.udc.acarballal.elmas.ffmpeg.encoder;

import es.udc.acarballal.elmas.ffmpeg.encoder.codecs.AbstractCodec;
import es.udc.acarballal.elmas.ffmpeg.encoder.codecs.AudioLayer;
import es.udc.acarballal.elmas.ffmpeg.encoder.codecs.VideoLayer;

public class FlvEncoder extends AbstractEncoder{
	
	private static final String TIME_PARAMETER ="ffmpeg/maxTime";

	private static final String FLV_EXTENSION_PARAMETER = "ffmpeg/flv/extension";
	private static final String FLV_VIDEOFORMAT_PARAMETER = "ffmpeg/flv/videoformat";
	private static final String FLV_VIDEOFRAMERATE_PARAMETER = "ffmpeg/flv/videoframerate";
	private static final String FLV_VIDEOSIZE_PARAMETER = "ffmpeg/flv/videosize";
	private static final String FLV_AUDIOCODEC_PARAMETER = "ffmpeg/flv/audiocodec";
	private static final String FLV_AUDIOBITRATE_PARAMETER = "ffmpeg/flv/audiobitrate";
	private static final String FLV_AUDIOCHANNELS_PARAMETER = "ffmpeg/flv/audiochannels";
	private static final String FLV_AUDIOFRECUENCE_PARAMETER = "ffmpeg/flv/audiofrecuence";
	
	public FlvEncoder(String input, String output) {
		super(input, output, getParameter(FLV_EXTENSION_PARAMETER));
	}

	public AbstractCodec getCodec(){
		int time = new Integer(getParameter(TIME_PARAMETER));
		
		String vf = getParameter(FLV_VIDEOFORMAT_PARAMETER);
		int fr = getIntParameter(FLV_VIDEOFRAMERATE_PARAMETER);
		String s = getParameter(FLV_VIDEOSIZE_PARAMETER);
		
		String acodec = getParameter(FLV_AUDIOCODEC_PARAMETER);
		int ab = getIntParameter(FLV_AUDIOBITRATE_PARAMETER);
		int ac = getIntParameter(FLV_AUDIOCHANNELS_PARAMETER);
		int f = getIntParameter(FLV_AUDIOFRECUENCE_PARAMETER);
		
		AbstractCodec codec = new VideoLayer(new AudioLayer(getBasicCodec(),
					acodec, ab, ac, f),	vf, fr, s, time);
		
		return codec;
		
	}
}
