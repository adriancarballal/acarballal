package es.udc.acarballal.elmas.model.vote;

import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface VoteDao extends GenericDao<Vote, Long>{

	public VOTE_TYPES findVoteMeanByVideoId();
	
	public boolean alreadyVoted(Long videoId, Long userProfileId);
}
