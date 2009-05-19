package es.udc.acarballal.elmas.model.videoservice;

import java.util.Calendar;
import java.util.List;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface VideoService {
	
	public Long addVideo(long userId, String title, String comment, 
			String snapshot, String original, String flvVideo, 
			String rtVideo, Calendar date)
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public Long commentVideo(Long commentatorId, Long videoId, 
			String comment,	Calendar date) throws InstanceNotFoundException, 
			InvalidOperationException;
	 
	public Long complaintOfVideo(Long videoId, Long userProfileId) 
		throws InstanceNotFoundException;
	
	public boolean isComplaintedBy(Long userId, Long videoId);
	
	public boolean isVideoCommentComplaintedBy(Long userId, Long videoCommentId);
	
	public Long complaintOfVideoComment(Long videoCommentId, Long userProfileId) 
		throws InstanceNotFoundException;
	
	public void deleteVideo(long videoId, long userId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;

	public void deleteVideoComment(Long commentId, Long userProfileId)
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public List<Video> findMostVoted(Calendar startDate, Calendar endDate, int count);
	
	public List<Video> findMostVoted(int count);
	
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) 
		throws InstanceNotFoundException;
	
	public Video findVideoById(long videoId) throws InstanceNotFoundException;
	
	public VideoCommentBlock findVideoCommentsByUserId(Long userId, 
			int startIndex, int count);

	public VideoCommentBlock findVideoCommentsByVideoId(Long videoId, 
			int startIndex, int count);

	public VideoBlock findVideosByTitle(String keys, int startIndex, int count);
	
	public VideoBlock findVideosByUser(Long userId, int startIndex, int count);
	
	public int getNumberVotesRemaining(Long userProfileId) 
		throws InstanceNotFoundException;
	
	public boolean isVideoVotable(Long videoId, Long userProfileId) 
		throws InstanceNotFoundException;
	
	public void voteVideo(VOTE_TYPES vote, Long userProfileId, Long videoId) 
			throws InstanceNotFoundException, VideoAlreadyVotedException, 
			InvalidOperationException;
	
	public VideoBlock findFavourites(Long userId, int startIndex, int count) 
		throws InstanceNotFoundException;
	
	public Long addToFavourites(Long userId, Long videoId)
		throws InstanceNotFoundException, DuplicateInstanceException;
	
	public void removeFromFavourites(Long userId, Long videoId) 
		throws InstanceNotFoundException;
	
	public boolean isFavourite(Long userId, Long videoId);
}
