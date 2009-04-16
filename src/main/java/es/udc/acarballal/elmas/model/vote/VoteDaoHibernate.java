package es.udc.acarballal.elmas.model.vote;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VoteDaoHibernate extends
		GenericDaoHibernate<Vote, Long> implements VoteDao{

	public boolean alreadyVoted(Long videoId, Long userProfileId){
		
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Vote c " +
					"WHERE c.video.videoId=:videoId AND c.voter.userProfileId=:userProfileId").
					setParameter("videoId", videoId).
					setParameter("userProfileId", userProfileId).
			uniqueResult();
		return count>0;
	}
	
	public List<Video> findMostVoted(Calendar startDate, Calendar endDate, int count){
		
		return (List<Video>)getSession().createQuery(
			"SELECT v.video from Vote v " +
			"WHERE v.date >= :startDate AND v.date <= :endDate " +
			"group by v.video.videoId order by AVG(v.vote) DESC"). 
			setParameter("startDate", startDate).		
			setParameter("endDate", endDate).
			setFirstResult(0).setMaxResults(count).list();
	}
	
	public List<Video> findMostVoted(int count){
		return (List<Video>)getSession().createQuery(
				"select v.video from Vote v " +
				"group by v.video.videoId order by AVG(v.vote) DESC"). 
				setFirstResult(0).setMaxResults(count).list();
	}
	
	public int votesRemaining(Long userProfileId, Calendar today){
		Calendar startDate = (Calendar)today.clone();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.AM_PM, Calendar.AM);
		startDate.set(Calendar.MILLISECOND, 0);
		Calendar endDate = (Calendar)today.clone();
		endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.AM_PM, Calendar.PM);
		endDate.set(Calendar.MILLISECOND, 999);
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Vote c " +
					"WHERE c.voter.userProfileId=:userProfileId AND " +
                "c.date >= :startDate AND c.date <= :endDate").
					setParameter("userProfileId", userProfileId).
					setCalendar("startDate", startDate).
	                setCalendar("endDate", endDate).
			uniqueResult();
		return MAX_VOTES_PER_WEEK - ((int) count);
	}
	
}
