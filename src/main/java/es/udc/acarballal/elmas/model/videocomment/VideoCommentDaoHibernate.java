package es.udc.acarballal.elmas.model.videocomment;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VideoCommentDaoHibernate  extends
GenericDaoHibernate<VideoComment, Long> implements VideoCommentDao{

	public List<VideoComment> findVideoCommentsByUserId(Long userId, 
			int startIndex, int count){
		return getSession().createQuery("SELECT c FROM VideoComment c " +
		"WHERE c.commentator.userProfileId=:userId ORDER BY c.date").
		setParameter("userId", userId).		
		setFirstResult(startIndex).setMaxResults(count).list();
	}
	
	public List<VideoComment> findVideoCommentsByVideoId(Long videoId, 
			int startIndex, int count){
		return getSession().createQuery("SELECT c FROM VideoComment c " +
		"WHERE c.video.videoId=:videoId ORDER BY c.date").
		setParameter("videoId", videoId).		
		setFirstResult(startIndex).setMaxResults(count).list();
	}

}
