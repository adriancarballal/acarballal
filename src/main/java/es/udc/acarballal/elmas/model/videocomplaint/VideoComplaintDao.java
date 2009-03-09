package es.udc.acarballal.elmas.model.videocomplaint;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface VideoComplaintDao extends GenericDao<VideoComplaint, Long>{

	public int countVideoComplaints();
	
	public List<VideoComplaint> findVideoComplaints(int startIndex, int count);
	
	public boolean hasComplaint(Long userId, Long videoId);
	
}
