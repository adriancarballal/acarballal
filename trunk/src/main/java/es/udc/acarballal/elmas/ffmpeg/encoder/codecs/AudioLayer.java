package es.udc.acarballal.elmas.ffmpeg.encoder.codecs;

public class AudioLayer extends CodecLayer{

	private String audioCodec = "";
	private String audioBitRate = "";
	private String audioChannels = "";
	private String audioFrecuence = "";
	
	public AudioLayer(AbstractCodec component, String audioCodec, int audioBitRate,
			int audioChannels, int audioFrecuence) {
		super(component);
		if(audioCodec!=null) this.audioCodec = " -acodec " + audioCodec;
		if(audioBitRate>0) this.audioBitRate = " -ab " + audioBitRate;
		if(audioChannels>0) this.audioChannels = " -ac " + audioChannels;
		if(audioFrecuence>0) this.audioFrecuence = " -ar " + audioFrecuence;
	}
	
	public String generateAudioOptions(){
		return super.generateAudioOptions() + audioCodec + audioBitRate 
			+ audioChannels + audioFrecuence;
	}

}
