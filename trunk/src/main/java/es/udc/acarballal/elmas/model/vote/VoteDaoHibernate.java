package es.udc.acarballal.elmas.model.vote;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class VoteDaoHibernate extends
		GenericDaoHibernate<Vote, Long> implements VoteDao{

	public VOTE_TYPES findVoteMeanByVideoId() {
		//Necesitamos hacer un groupBy
		return null;
	}
	
	public boolean alreadyVoted(Long videoId, Long userProfileId){
		
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Vote c " +
					"WHERE c.video.videoId=:videoId AND c.voter.userProfileId=:userProfileId").
					setParameter("videoId", videoId).
					setParameter("userProfileId", userProfileId).
			uniqueResult();
		return count>0;
	}
	
	public int votesRemaining(Long userProfileId, Calendar today){
		
		Calendar startDate = (Calendar)today.clone();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		long count = 
			(Long) getSession().createQuery("SELECT count(c) FROM Vote c " +
					"WHERE c.voter.userProfileId=:userProfileId AND " +
                "c.date >= :startDate AND c.date <= :endDate").
					setParameter("userProfileId", userProfileId).
					setCalendar("startDate", startDate).
	                setCalendar("endDate", today).
			uniqueResult();
		
		return MAX_VOTES_PER_WEEK - ((int) count);
	}
	
}
