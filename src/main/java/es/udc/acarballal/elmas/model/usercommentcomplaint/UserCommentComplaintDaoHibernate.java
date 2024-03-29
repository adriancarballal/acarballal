package es.udc.acarballal.elmas.model.usercommentcomplaint;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class UserCommentComplaintDaoHibernate extends
		GenericDaoHibernate<UserCommentComplaint, Long> implements UserCommentComplaintDao {
	
	public int countUserCommentComplaints(){
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM UserCommentComplaint c ").
			uniqueResult(); 
		return (int)count;
	}
	
	public List<UserCommentComplaint> findUserCommentComplaints(int startIndex, int count){
		return getSession().createQuery("SELECT c " +
				"FROM UserCommentComplaint c ORDER BY c.date desc").
		setFirstResult(startIndex).setMaxResults(count).list();
	}
	
	public boolean hasComplaint(Long userId, Long userCommentId){
		long count = 
			(Long) getSession().createQuery("SELECT count(c) " +
					"FROM UserCommentComplaint c " +
					"WHERE c.reference.commentId=:userCommentId AND " +
					"c.complainer.userProfileId=:userProfileId").
					setParameter("userCommentId", userCommentId).
					setParameter("userProfileId", userId).
			uniqueResult();
		return count>0;
	}

}