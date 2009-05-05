package es.udc.acarballal.elmas.ffmpeg.encoder;

import es.udc.acarballal.elmas.ffmpeg.encoder.codecs.AbstractCodec;
import es.udc.acarballal.elmas.ffmpeg.encoder.codecs.AudioLayer;
import es.udc.acarballal.elmas.ffmpeg.encoder.codecs.VideoLayer;

public class RPlayerEncoder extends AbstractEncoder{
	
	private static final String TIME_PARAMETER ="ffmpeg/maxTime";
	
	private static final String RPLAYER_EXTENSION_PARAMETER = "ffmpeg/rplayer/extension";
	private static final String RPLAYER_VIDEOFORMAT_PARAMETER = "ffmpeg/rplayer/videoformat";
	private static final String RPLAYER_VIDEOFRAMERATE_PARAMETER = "ffmpeg/rplayer/videoframerate";
	private static final String RPLAYER_VIDEOSIZE_PARAMETER = "ffmpeg/rplayer/videosize";
	private static final String RPLAYER_AUDIOCODEC_PARAMETER = "ffmpeg/rplayer/audiocodec";
	private static final String RPLAYER_AUDIOBITRATE_PARAMETER = "ffmpeg/rplayer/audiobitrate";
	private static final String RPLAYER_AUDIOCHANNELS_PARAMETER = "ffmpeg/rplayer/audiochannels";
	private static final String RPLAYER_AUDIOFRECUENCE_PARAMETER = "ffmpeg/rplayer/audiofrecuence";

	
	public RPlayerEncoder(String input, String output) {
		super(input, output, getParameter(RPLAYER_EXTENSION_PARAMETER));
	}

	public AbstractCodec getCodec(){

		int time = new Integer(getParameter(TIME_PARAMETER));
		
		String vf = getParameter(RPLAYER_VIDEOFORMAT_PARAMETER);
		int fr = getIntParameter(RPLAYER_VIDEOFRAMERATE_PARAMETER);
		String s = getParameter(RPLAYER_VIDEOSIZE_PARAMETER);
		
		String acodec = getParameter(RPLAYER_AUDIOCODEC_PARAMETER);
		int ab = getIntParameter(RPLAYER_AUDIOBITRATE_PARAMETER);
		int ac = getIntParameter(RPLAYER_AUDIOCHANNELS_PARAMETER);
		int f = getIntParameter(RPLAYER_AUDIOFRECUENCE_PARAMETER);
		
		AbstractCodec codec = new VideoLayer(new AudioLayer(getBasicCodec(),
					acodec, ab, ac, f),	vf, fr, s, time);
		
		return codec;
		
	}
}