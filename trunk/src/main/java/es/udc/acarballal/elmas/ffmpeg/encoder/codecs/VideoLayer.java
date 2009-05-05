package es.udc.acarballal.elmas.ffmpeg.encoder.codecs;

public class VideoLayer extends CodecLayer{

	private String format = "";
	private String frameRate = "";
	private String time = "";
	private String size = "";
	
	public VideoLayer(AbstractCodec component, String format, int frameRate, 
			String size, int time) {
		super(component);
		if(format!=null) this.format = " -f " + format;
		if(frameRate>0) this.frameRate = " -r " + frameRate;
		if(size!=null) this.size = " -s " + size;
		if(time>0) this.time = " -t "  + time;		
	}
	
	public String generateVideoOptions(){
		return super.generateVideoOptions() + format + frameRate + size + time;
	}

}
