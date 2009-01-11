package es.udc.acarballal.elmas.model.usercommentcomplaint;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface UserCommentComplaintDao extends GenericDao<UserCommentComplaint, Long>{

	public int countUserCommentComplaints();
	
	public List<UserCommentComplaint> findUserCommentComplaints(int startIndex, int count);
	
}