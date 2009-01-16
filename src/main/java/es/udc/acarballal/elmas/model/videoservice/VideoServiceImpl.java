package es.udc.acarballal.elmas.model.videoservice;

import java.util.Calendar;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.video.VideoDao;
import es.udc.acarballal.elmas.model.videocomment.VideoComment;
import es.udc.acarballal.elmas.model.videocomment.VideoCommentDao;
import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaint;
import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaintDao;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaintDao;
import es.udc.acarballal.elmas.model.vote.Vote;
import es.udc.acarballal.elmas.model.vote.VoteDao;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class VideoServiceImpl implements VideoService{

	private VideoDao videoDao;
	private UserProfileDao userProfileDao;
	private VideoCommentDao videoCommentDao;
	private VoteDao voteDao;
	private VideoComplaintDao videoComplaintDao;
	private VideoCommentComplaintDao videoCommentComplaintDao;

	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}
	
	public void setUserProfileDao(UserProfileDao userProfileDao){
		this.userProfileDao = userProfileDao;
	}
	
	public void setVideoCommentDao(VideoCommentDao videoCommentDao){
		this.videoCommentDao = videoCommentDao;
	}
	
	public void setVoteDao(VoteDao voteDao){
		this.voteDao = voteDao;
	}
	
	public void setVideoComplaintDao(VideoComplaintDao videoComplaintDao){
		this.videoComplaintDao = videoComplaintDao;
	}
	
	public void setVideoCommentComplaintDao(
			VideoCommentComplaintDao videoCommentComplaintDao){
		this.videoCommentComplaintDao = videoCommentComplaintDao;
	}
	
	public Long addVideo(long userId, String title, String comment, 
			String snapshot,String original, String flvVideo, 
			String mp4Video, Calendar date)
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile = userProfileDao.find(userId);
		
		if(userProfile.getPrivileges()!=Privileges_TYPES.COMPETITOR){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		Video video = new Video(userProfile, title, comment, snapshot, original,
				flvVideo, mp4Video, date);
		videoDao.create(video);
		return video.getVideoId();
	}
	
	@Transactional(readOnly = true)
	public Video findVideoById(long videoId) throws InstanceNotFoundException{
		return videoDao.find(videoId);
	}
	
	public void deleteVideo(long videoId, long userId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{

		UserProfile userProfile = userProfileDao.find(userId);
		if(userProfile.getPrivileges()!=Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(userProfile.getLoginName()); 
		}
		videoDao.remove(videoId);
	}
	
	public Long commentVideo(Long commentatorId, Long videoId, 
			String comment,	Calendar date) throws InstanceNotFoundException, 
			InsufficientPrivilegesException, InvalidOperationException{

		UserProfile commentatorUser = userProfileDao.find(commentatorId);
		Video video = videoDao.find(videoId);
		if(commentatorUser.getPrivileges()==Privileges_TYPES.NONE ||
				commentatorUser.getPrivileges()==Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(commentatorUser.getLoginName());
		}
		if(commentatorUser.getUserProfileId() == video.getUserProfile().getUserProfileId()){
			throw new InvalidOperationException("Cannot comment your own video " + 
					commentatorUser.getLoginName());
		}
		VideoComment videoComment = new VideoComment(video, commentatorUser, 
				comment, date);
		videoCommentDao.create(videoComment);
		return videoComment.getCommentId();
	}
	
	//A�adir un adminService para este servicio?
	public void deleteVideoComment(Long commentId, Long userProfileId)
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		VideoComment comment = videoCommentDao.find(commentId);
		UserProfile userProfile = userProfileDao.find(userProfileId);
		if(!comment.getCommentator().getUserProfileId().equals((userProfileId))
				&& userProfile.getPrivileges()!=Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		videoCommentDao.remove(commentId);

	}
	
	public void voteVideo(Long userProfileId, Long videoId, VOTE_TYPES vote, 
			Calendar date) throws InstanceNotFoundException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		if(userProfile.getPrivileges()!=Privileges_TYPES.VOTER){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		Video video = videoDao.find(videoId);
		if(video.getUserProfile().equals(userProfile)){
			throw new InvalidOperationException("Cannot vote your own video");
		}
		Vote newVote = new Vote(video, userProfile, vote, date);
		
		voteDao.create(newVote);
		
	}
	
	@Transactional(readOnly = true)
	public VideoBlock findVideosByTitle(String keys, int startIndex, int count){
		
		List<Video> videos = videoDao.findByTitle(keys, startIndex, count+1);

		boolean existMoreVideos = videos.size() == (count + 1);

		if (existMoreVideos) {
			videos.remove(videos.size() - 1);
		}
		
		return new VideoBlock(videos, existMoreVideos);

	}
	
	@Transactional(readOnly = true)
	public VideoBlock findVideosByUser(Long userId, int startIndex, int count){
		
		List<Video> videos = videoDao.findByUser(userId, startIndex, count+1);

		boolean existMoreVideos = videos.size() == (count + 1);

		if (existMoreVideos) {
			videos.remove(videos.size() - 1);
		}
		
		return new VideoBlock(videos, existMoreVideos);
		
	}
		
	@Transactional(readOnly = true)
	public VideoCommentBlock findVideoCommentsByVideoId(Long videoId, 
			int startIndex, int count){
		
		List<VideoComment> comments = 
			videoCommentDao.findVideoCommentsByVideoId(videoId, startIndex, count+1);

		boolean existMoreVideoComments = comments.size() == (count + 1);

		if (existMoreVideoComments) {
			comments.remove(comments.size() - 1);
		}
		
		return new VideoCommentBlock(comments, existMoreVideoComments);
	}

	@Transactional(readOnly = true)
	public VideoCommentBlock findVideoCommentsByUserId(Long userId, 
			int startIndex, int count){
		
		List<VideoComment> comments = 
			videoCommentDao.findVideoCommentsByUserId(userId, startIndex, count+1);

		boolean existMoreVideoComments = comments.size() == (count + 1);

		if (existMoreVideoComments) {
			comments.remove(comments.size() - 1);
		}
		
		return new VideoCommentBlock(comments, existMoreVideoComments);
	}
	
	public void complaintOfVideo(Long videoId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		Video video = videoDao.find(videoId);
		if(userProfile.getPrivileges()==Privileges_TYPES.NONE){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		VideoComplaint complaint = 
			new VideoComplaint(video, userProfile, Calendar.getInstance());
		videoComplaintDao.create(complaint);
	}
	
	public void complaintOfVideoComment(Long videoCommentId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		VideoComment comment = videoCommentDao.find(videoCommentId);
		if(userProfile.getPrivileges()==Privileges_TYPES.NONE){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		VideoCommentComplaint complaint =
			new VideoCommentComplaint(comment, userProfile, Calendar.getInstance());
		videoCommentComplaintDao.create(complaint);
	}
	
	public void voteVideo(VOTE_TYPES vote, Long userProfileId, Long videoId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
				VideoAlreadyVotedException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		Video video = videoDao.find(videoId);
		if(userProfile.getPrivileges()==Privileges_TYPES.NONE){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		if(voteDao.alreadyVoted(video.videoId, userProfileId)){
			throw new VideoAlreadyVotedException(userProfile.getLoginName(), video.getTitle());
		}
		Vote newVote = new Vote(video, userProfile, vote, Calendar.getInstance());
		voteDao.create(newVote);
	}
	
	public boolean isVideoVotable(Long videoId, Long userProfileId) 
			throws InstanceNotFoundException{
		userProfileDao.find(userProfileId);
		return voteDao.alreadyVoted(videoId, userProfileId);
	}
	
	public int getNumberVotesRemaining(Long userProfileId) throws InstanceNotFoundException{
		userProfileDao.find(userProfileId);
		return voteDao.votesRemaining(userProfileId, Calendar.getInstance());
	}
	
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) throws InstanceNotFoundException{
		userProfileDao.find(userProfileId);
		return videoDao.findRandomVotableVideo(userProfileId, preSelected);
	}
}
