package es.udc.acarballal.elmas.model.vote;

import java.util.Calendar;
import java.util.List;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface VoteDao extends GenericDao<Vote, Long>{
	
	final int MAX_VOTES_PER_WEEK = 10;
	
	public VOTE_TYPES findVoteMeanByVideoId();
	
	public boolean alreadyVoted(Long videoId, Long userProfileId);
	
	public int votesRemaining(Long userProfileId, Calendar today);
	
	public List<Video> findMostVoted(Calendar startDate, Calendar endDate, int count);
	
	public List<Video> findMostVoted(int count);
}
