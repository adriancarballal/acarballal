package es.udc.acarballal.elmas.ffmpeg;

public class VideoPostProcessing extends Thread{
	
	static String ORIGINAL_FILE = "C:\\input3.mp4";
	private String file;
	
	public VideoPostProcessing(String file){
		this.file = file;
	}

	public void run(){
		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(new Long(-1), "nuevo", "nuevocomment", file);
		taskList.addProcess(task);		
	}
}
