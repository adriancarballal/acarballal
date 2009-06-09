package es.udc.acarballal.elmas.model.video;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface VideoDao extends GenericDao<Video, Long>{
		
	public List<Video> findByTitle(String key, int startIndex, int count);
	
	public List<Video> findByUser(Long userId, int startIndex, int count);
	
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) 
		throws InstanceNotFoundException;
	
	public boolean addedVideo(Long userProfileId, Calendar today);
}
