package es.udc.acarballal.elmas.model.vote;

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
	
}
