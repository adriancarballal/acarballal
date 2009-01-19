package es.udc.acarballal.elmas.model.videoservice;

import java.util.Calendar;
import java.util.List;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface VideoService {
	
	public Long addVideo(long userId, String title, String comment, 
			String snapshot, String original, String flvVideo, 
			String mp4Video, Calendar date)
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	//A�adir un adminService para este servicio?
	public void deleteVideo(long videoId, long userId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	 
	public Video findVideoById(long videoId) throws InstanceNotFoundException;
	
	public Long commentVideo(Long commentatorId, Long videoId, 
			String comment,	Calendar date) throws InstanceNotFoundException, 
			InsufficientPrivilegesException, InvalidOperationException;
	
	//A�adir un adminService para este servicio?
	public void deleteVideoComment(Long commentId, Long userProfileId)
			throws InstanceNotFoundException, InsufficientPrivilegesException;

	public void voteVideo(VOTE_TYPES vote, Long userProfileId, Long videoId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			VideoAlreadyVotedException;
	
	public boolean isVideoVotable(Long videoId, Long userProfileId) 
		throws InstanceNotFoundException;
	
	public int getNumberVotesRemaining(Long userProfileId) 
		throws InstanceNotFoundException;
	
	public VideoBlock findVideosByTitle(String keys, int startIndex, int count);
	
	public VideoBlock findVideosByUser(Long userId, int startIndex, int count);
	
	public VideoCommentBlock findVideoCommentsByVideoId(Long videoId, 
			int startIndex, int count);

	public VideoCommentBlock findVideoCommentsByUserId(Long userId, 
			int startIndex, int count);

	public void complaintOfVideo(Long videoId, Long userProfileId) 
		throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public void complaintOfVideoComment(Long videoCommentId, Long userProfileId) 
		throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) 
		throws InstanceNotFoundException;
	
	public List<Video> findMostVoted(Calendar startDate, Calendar endDate, int count);
	
	public List<Video> findMostVoted(int count);
}
