package es.udc.acarballal.elmas.ffmpeg.encoder.codecs;

public class Codec extends AbstractCodec{
	
	private String input;
	private String output;
	private String extension;
	
	public Codec(String input, String output, String extension){
		this.input = input;
		this.output = output;
		this.extension = extension;
	}
	
	public String generateVideoOptions(){
		return "";
	}
	public String generateAudioOptions(){
		return "";
	}
	public String generateImageOptions(){
		return "";
	}
	
	public String getOutput(){
		return output + "." + extension;
	}
	
	public String getInput(){
		return input;
	}



}
