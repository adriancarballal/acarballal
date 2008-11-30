package es.udc.acarballal.elmas.model.videoservice;

import java.util.Calendar;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface VideoService {
	
	public Long addVideo(long userId, String title, String comment, 
			String snapshot, Calendar date)
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public void deleteVideo(long videoId, long userId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	 
	public Video findVideoById(long videoId) throws InstanceNotFoundException;

}
