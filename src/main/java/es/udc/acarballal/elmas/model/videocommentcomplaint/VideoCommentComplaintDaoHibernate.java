package es.udc.acarballal.elmas.model.videocommentcomplaint;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoCommentComplaintDaoHibernate extends
		GenericDaoHibernate<VideoCommentComplaint, Long> implements VideoCommentComplaintDao {
	
	public int countVideoCommentComplaints(){
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM VideoCommentComplaint c ").
			uniqueResult(); 
		return (int)count;
	}
	
	public List<VideoCommentComplaint> findVideoCommentComplaints(int startIndex, int count){
		return getSession().createQuery("SELECT c " +
				"FROM VideoCommentComplaint c ORDER BY c.date DESC").
		setFirstResult(startIndex).setMaxResults(count).list();
	}
	
	public boolean hasComplaint(Long userId, Long videoCommentId){
		long count = 
			(Long) getSession().createQuery("SELECT count(c) " +
					"FROM VideoCommentComplaint c " +
					"WHERE c.reference.commentId=:videoCommentId AND " +
					"c.complainer.userProfileId=:userProfileId").
					setParameter("videoCommentId", videoCommentId).
					setParameter("userProfileId", userId).
			uniqueResult();
		return count>0;
	}

}