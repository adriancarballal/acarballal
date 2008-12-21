package es.udc.acarballal.elmas.model.video;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface VideoDao extends GenericDao<Video, Long>{
		
	public List<Video> findByTitle(String key, int startIndex, int count);
	
	public List<Video> findByUser(Long userId, int startIndex, int count);
	
	public int getNumberOfVideosByUser(Long userId);
}
