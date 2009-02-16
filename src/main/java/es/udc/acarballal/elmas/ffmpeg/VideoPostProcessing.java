package es.udc.acarballal.elmas.ffmpeg;

public class VideoPostProcessing {
	
	static String ORIGINAL_FILE = "C:\\input3.mp4";

	public static void main(String[] args) {
		
		long tiempoInicio = System.currentTimeMillis();
		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(new Long(-1), "nuevo", "nuevocomment", ORIGINAL_FILE);
		taskList.addProcess(task);		
		long totalTiempo = System.currentTimeMillis() - tiempoInicio;
		System.out.println("[" + totalTiempo/60000 + " min (" + totalTiempo/1000 + "seg)]");
		
	}
}
