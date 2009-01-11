package es.udc.acarballal.elmas.model.videocommentcomplaint;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface VideoCommentComplaintDao extends GenericDao<VideoCommentComplaint, Long>{

	public int countVideoCommentComplaints();
	
	public List<VideoCommentComplaint> findVideoCommentComplaints(int startIndex, int count);
	
}
