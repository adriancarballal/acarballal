package es.udc.acarballal.elmas.ffmpeg.encoder;

public interface IVideoEncoder{
	
	public enum VIDEO_SIZE 		{sqcif, qcif, cif, qvga, vga, svga, wvga, ega, hd720, hd180};
	public enum VIDEO_CODEC 	{libx264};
	//public enum ASPECT_RATIO 	{("4:3"),("16:9")};
	//public enum FRAME_RATE		{_25, _30};
	
	public enum AUDIO_CODEC 	{libfaac};
	//public enum AUDIO_FREQUENCE	{_44100, _22050, _11025};
	
	
	public void setVideoSize(VIDEO_SIZE size);
	public void setVideoCodec(VIDEO_CODEC videoCodec);
	public void setVideoBitRate(int bitRate);
	public void setAspectRatio(String ratio);
	public void setFrameRate(int frameRate);
	
	public void setAudioCodec(AUDIO_CODEC audioCodec);
	public void setAudioBitRate(int bitRate);
	public void setAudioFrecuence(int frecuence);
	public void setAudioChannels(int channels);
	
	String generateEncodingCommand();

}
