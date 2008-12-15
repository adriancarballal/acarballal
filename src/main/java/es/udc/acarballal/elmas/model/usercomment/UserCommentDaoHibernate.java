package es.udc.acarballal.elmas.model.usercomment;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class UserCommentDaoHibernate extends
	GenericDaoHibernate<UserComment, Long> implements UserCommentDao{

	public List<UserComment> findCommentByCommentator(Long userProfileId,
			int startIndex, int count){
		return getSession().createQuery("SELECT u FROM UserComment u " +
		"WHERE u.commentator.userProfileId=:commentator ORDER BY u.date").
		setParameter("commentator", userProfileId).		
		setFirstResult(startIndex).setMaxResults(count).list();
	}
	
	public List<UserComment> findCommentByCommented(Long userProfileId,
			int startIndex, int count){
		return getSession().createQuery("SELECT u FROM UserComment u " +
		"WHERE u.commented.userProfileId=:commented ORDER BY u.date DESC").
		setParameter("commented", userProfileId).		
		setFirstResult(startIndex).setMaxResults(count).list();
	}
}
