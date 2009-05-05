package es.udc.acarballal.elmas.ffmpeg.encoder.codecs;

public class ImageLayer extends CodecLayer{
	
	private String time = "";
	private String startSecond = "";
	private String frames = "";
	
	public ImageLayer(AbstractCodec component, int time, int startSecond, int frames) {
		super(component);
		if(time>0) this.time = " -t " + time;
		if(startSecond>0) this.startSecond = " -ss " + startSecond;
		if(frames>0) this.frames = " -vframes " + frames;
	}

	public String generateImageOptions(){
		return super.generateImageOptions() + time + startSecond + frames;
	}
}
