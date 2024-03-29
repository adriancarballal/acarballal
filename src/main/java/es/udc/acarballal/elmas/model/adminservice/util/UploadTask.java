package es.udc.acarballal.elmas.model.adminservice.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import es.udc.acarballal.elmas.ffmpeg.encoder.AbstractEncoder;
import es.udc.acarballal.elmas.ffmpeg.encoder.AbstractEncoderFactory;
import es.udc.acarballal.elmas.ffmpeg.encoder.EncoderFactory;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;
import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.ffmpeg.process.Process;
import es.udc.acarballal.elmas.ffmpeg.process.util.DirectoryGenerator;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class UploadTask {
	
	private static final String TEMPORAL_DIRECTORY_PARAMETER = "temporal/directory";
	private static final String CONTAINER_DIRECTORY_PARAMETER = "container/directory";
	private static final String UNAVAILABLE_PICTURE_PARAMETER = "unavailable/snapshot";
	private static String containerDirectoryPath;
	private static String tempDirectoryPath;
	private String temporalDirectory;
	private static String unavailableSnapshot;
	
	private static final String FILE_FORMAT = "ISO8859-1";
	private static final String flvVideoLog = "flvEncoding.log";
	private static final String infoLog = "userInfo.log";
	private static final String rtVideoLog = "3gpEncoding.log";
	private static final String snapshotLog = "snapshot.log";
	
	static{
		try {
			tempDirectoryPath = 
				ConfigurationParametersManager.getParameter(TEMPORAL_DIRECTORY_PARAMETER);
			containerDirectoryPath = 
				ConfigurationParametersManager.getParameter(CONTAINER_DIRECTORY_PARAMETER);
			unavailableSnapshot =
				ConfigurationParametersManager.getParameter(UNAVAILABLE_PICTURE_PARAMETER);
		} catch (MissingConfigurationParameterException e) {
			e.printStackTrace();
		}
	}
	private String autogeneratedCode = new String();
	private String comment;
	protected OutputStreamWriter logWriter = null;
	private String originalFile;
	private List<IProcess> processes;
	private String title;
	private Long userId;
	private VideoService videoService;
	private UserService userService;
	
	public UploadTask(Long userId, String title, String comment, 
			String originalFile, UserService userService, 
			VideoService videoService){
		
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.originalFile = originalFile;
		this.processes = new ArrayList<IProcess>();
		this.userService = userService;
		this.videoService = videoService;
		createProcesses();
	}
	
	private void createProcesses(){
		
		AbstractEncoderFactory factory = new EncoderFactory();
		
		//Temporal directories
		int i = originalFile.lastIndexOf("\\");
		autogeneratedCode = originalFile.substring(i-DirectoryGenerator.getStringLength(),i);
		autogeneratedCode =  autogeneratedCode.split("[.]")[0];
		
		File dir = new File(tempDirectoryPath + autogeneratedCode + "\\"); dir.mkdir();
		temporalDirectory = dir.getAbsolutePath() + "\\";
		
		//UserInfo
		String logFile = temporalDirectory + infoLog;
		try {
			logWriter = new OutputStreamWriter(new FileOutputStream(logFile), FILE_FORMAT);
			logWriter.write("UserId : " + userId + "\n");
			logWriter.write("Title  : " + title + "\n");
			logWriter.write("Comment: " + comment  +"\n");
			logWriter.flush();
			logWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// FLV VIDEO
		String flvName = temporalDirectory + autogeneratedCode;
		String flvLog = temporalDirectory + flvVideoLog;
		AbstractEncoder flvEncoder = factory.createFlvEncoder(originalFile, flvName);
			IProcess flvProcess = (IProcess) new Process(flvLog, flvEncoder);
		processes.add(flvProcess);
		
		// RealTime VIDEO
		String miniName = temporalDirectory + autogeneratedCode;
		String miniLog = temporalDirectory + rtVideoLog;
		AbstractEncoder rpEncoder = 
			factory.createRealPlayerEncoder(flvName + ".flv", miniName);
		IProcess rpProcess = (IProcess) new Process(miniLog, rpEncoder);
		processes.add(rpProcess);
		
		//Predefined Snapshot
		IProcess moveSnap = new FileCopy(unavailableSnapshot, 
				temporalDirectory + autogeneratedCode + ".jpg");
		processes.add(moveSnap);
		
		//SnapSHOT
		String snapshotName = temporalDirectory + autogeneratedCode;
		String snapLog = temporalDirectory + snapshotLog;
		AbstractEncoder snapEncoder =
			factory.createSnapshotEncoder(miniName + ".3gp", snapshotName);
		IProcess snapProcess = (IProcess) new Process(snapLog, snapEncoder);
		processes.add(snapProcess);
		
		//DELETE LOGS
		List<File> logs = new ArrayList<File>();
		logs.add(new File(flvLog));
		logs.add(new File(miniLog));
		logs.add(new File(snapLog));
		logs.add(new File(logFile));
		IProcess deleteLogFiles = new LogDelete(logs);
		processes.add(deleteLogFiles);
		
		//MOVE FOLDER
		String destinationFolder = containerDirectoryPath + "\\" + autogeneratedCode ;
		IProcess moveFolder = new MoveTempFolder(temporalDirectory, destinationFolder);
		processes.add(moveFolder);
		
		//VIDEO ACCEPTANCE
		IProcess accept = new VideoAccept(userId, title, comment, originalFile, destinationFolder);
		((VideoAccept)accept).setVideoService(videoService);
		processes.add(accept);

		//Send Admin Confirmation Message
		IProcess confirmation = new ConfirmationMessage(userService, userId, 
				title + ": Video subido con exito");
		processes.add(confirmation);
	}
	
	public void execute(){
		boolean flag = true;
		while(flag && processes.size()>0){
			flag = processes.get(0).execute();
			if(flag) processes.remove(0);
		}
		if(!flag){
			
			/*ASEGURARSE DE QUE LOS LOGS SE DESCONECTAN*/
			undo();
			processes.clear();
			IProcess error = new ConfirmationMessage(userService, userId, 
					title + ": Ha ocurrido un error. Vuelva a subirlo por favor.");
			processes.add(error);
			IProcess deleteTemp = new DeleteTempFolder(temporalDirectory);
			processes.add(deleteTemp);
			processes.get(0).execute();
			processes.get(1).execute();
			processes.clear();
		}
	}

	public void undo(){
		/*ASEGURARSE DE QUE LOS LOGS SE DESCONECTAN*/
		for (int i = 0; i < processes.size(); i++) {
			processes.get(i).undo();		
		}
		
	}
}
