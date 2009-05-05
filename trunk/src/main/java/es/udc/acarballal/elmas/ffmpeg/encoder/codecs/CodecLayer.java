package es.udc.acarballal.elmas.ffmpeg.encoder.codecs;

public abstract class CodecLayer extends AbstractCodec{
	
	public AbstractCodec component;
	
	public CodecLayer(AbstractCodec component){
		this.component = component;
	}
	
	public String generateVideoOptions(){
		return component.generateVideoOptions();
	}
	public String generateAudioOptions(){
		return component.generateAudioOptions();
	}
	public String generateImageOptions(){
		return component.generateImageOptions();
	}

	public String getOutput(){
		return component.getOutput();
	}
	
	public String getInput(){
		return component.getInput();
	}
}
