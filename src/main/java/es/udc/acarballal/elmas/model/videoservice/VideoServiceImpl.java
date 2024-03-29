package es.udc.acarballal.elmas.model.videoservice;

import java.util.Calendar;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;
import es.udc.acarballal.elmas.model.adminservice.util.DeleteTempFolder;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.favourite.Favourite;
import es.udc.acarballal.elmas.model.favourite.FavouriteDao;
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
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class VideoServiceImpl implements VideoService{

	private final static String CONTAINER_FOLDER_PARAMETER = "container/directory";
	
	private UserProfileDao userProfileDao;
	private VideoCommentComplaintDao videoCommentComplaintDao;
	private VideoCommentDao videoCommentDao;
	private VideoComplaintDao videoComplaintDao;
	private FavouriteDao favouriteDao;
	private VideoDao videoDao;
	private VoteDao voteDao;
	
		

	public Long addVideo(long userId, String title, String comment, 
			String snapshot,String original, String flvVideo, 
			String rtVideo, Calendar date)
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile = userProfileDao.find(userId);
		
		if(userProfile.getPrivileges()!=Privileges_TYPES.COMPETITOR &&
				userProfile.getPrivileges()!=Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		Video video = new Video(userProfile, title, comment, snapshot, original,
				flvVideo, rtVideo, date);
		videoDao.create(video);
		return video.getVideoId();
	}
	
	@Transactional(readOnly = true)
	public boolean addedVideo(Long userId, Calendar today) 
			throws InstanceNotFoundException{
		
		userProfileDao.find(userId);
		return videoDao.addedVideo(userId, today);
	}
	
	public Long commentVideo(Long commentatorId, Long videoId, 
			String comment,	Calendar date) throws InstanceNotFoundException, 
			InvalidOperationException{

		UserProfile commentatorUser = userProfileDao.find(commentatorId);
		Video video = videoDao.find(videoId);
		if(commentatorUser.getUserProfileId() == video.getUserProfile().getUserProfileId()){
			throw new InvalidOperationException("Cannot comment your own video " + 
					commentatorUser.getLoginName());
		}
		VideoComment videoComment = new VideoComment(video, commentatorUser, 
				comment, date);
		videoCommentDao.create(videoComment);
		return videoComment.getCommentId();
	}
	
	public Long complaintOfVideo(Long videoId, Long userProfileId) 
			throws InstanceNotFoundException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		Video video = videoDao.find(videoId);
		VideoComplaint complaint = 
			new VideoComplaint(video, userProfile, Calendar.getInstance());
		videoComplaintDao.create(complaint);
		return complaint.getComplaintId();
	}
	
	@Transactional(readOnly = true)
	public boolean isComplaintedBy(Long userId, Long videoId){
		return videoComplaintDao.hasComplaint(userId, videoId);
	}
	
	@Transactional(readOnly = true)
	public boolean isVideoCommentComplaintedBy(Long userId, Long videoCommentId){
		return videoCommentComplaintDao.hasComplaint(userId, videoCommentId);
	}
	
	public Long complaintOfVideoComment(Long videoCommentId, Long userProfileId) 
			throws InstanceNotFoundException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		VideoComment comment = videoCommentDao.find(videoCommentId);
		
		VideoCommentComplaint complaint =
			new VideoCommentComplaint(comment, userProfile, Calendar.getInstance());
		videoCommentComplaintDao.create(complaint);
		return complaint.getComplaintId();
	}
	
	public void deleteVideo(long videoId, long userId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{

		UserProfile userProfile = userProfileDao.find(userId);
		Video video = videoDao.find(videoId);
		if(userProfile.getPrivileges()!=Privileges_TYPES.ADMIN &&
				!userProfile.equals(video.getUserProfile())){
			throw new InsufficientPrivilegesException(userProfile.getLoginName()); 
		}
		
		/* Eliminamos la carpeta dentro del contenedor */
		String folder = video.getOriginal()
		.substring(1,video.getOriginal().lastIndexOf("/"));
		try {
			folder = ConfigurationParametersManager.
				getParameter(CONTAINER_FOLDER_PARAMETER) + "\\" + 
				folder.substring(folder.lastIndexOf("/")+1, folder.length());
		} catch (MissingConfigurationParameterException e) {}
		(new DeleteTempFolder(folder)).execute();
		videoDao.remove(videoId);
		
	}
	
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
	
	@Transactional(readOnly = true)
	public List<Video> findMostVoted(Calendar startDate, Calendar endDate, int count){
		return voteDao.findMostVoted(startDate, endDate, count);
	}
	
	@Transactional(readOnly = true)
	public List<Video> findMostVoted(int count){
		return voteDao.findMostVoted(count);
	}
	
	@Transactional(readOnly = true)
	public Video findRandomVotableVideo(Long userProfileId, int preSelected) throws InstanceNotFoundException{
		userProfileDao.find(userProfileId);
		return videoDao.findRandomVotableVideo(userProfileId, preSelected);
	}
	
	@Transactional(readOnly = true)
	public Video findVideoById(long videoId) 
			throws InstanceNotFoundException{
		
		return videoDao.find(videoId);
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
	public int getNumberVotesRemaining(Long userProfileId) throws InstanceNotFoundException{
		userProfileDao.find(userProfileId);
		return voteDao.votesRemaining(userProfileId, Calendar.getInstance());
	}

	@Transactional(readOnly = true)
	public boolean isVideoVotable(Long videoId, Long userProfileId) 
			throws InstanceNotFoundException{
		userProfileDao.find(userProfileId);
		return voteDao.alreadyVoted(videoId, userProfileId);
	}
	
	public void setUserProfileDao(UserProfileDao userProfileDao){
		this.userProfileDao = userProfileDao;
	}
	
	public void setVideoCommentComplaintDao(
			VideoCommentComplaintDao videoCommentComplaintDao){
		this.videoCommentComplaintDao = videoCommentComplaintDao;
	}
	
	public void setVideoCommentDao(VideoCommentDao videoCommentDao){
		this.videoCommentDao = videoCommentDao;
	}
	
	public void setVideoComplaintDao(VideoComplaintDao videoComplaintDao){
		this.videoComplaintDao = videoComplaintDao;
	}
	
	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}
	
	public void setVoteDao(VoteDao voteDao){
		this.voteDao = voteDao;
	}
	
	public void setFavouriteDao(FavouriteDao favouriteDao){
		this.favouriteDao = favouriteDao;
	}
	
	public void voteVideo(short vote, Long userProfileId, Long videoId) 
			throws InstanceNotFoundException, VideoAlreadyVotedException, 
			InvalidOperationException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		Video video = videoDao.find(videoId);
		if(video.getUserProfile().equals(userProfile)){
			throw new InvalidOperationException("Cannot vote your own video");
		}
		if(voteDao.alreadyVoted(video.getVideoId(), userProfileId)){
			throw new VideoAlreadyVotedException(userProfile.getLoginName(), video.getTitle());
		}
		Vote newVote = new Vote(video, userProfile, vote, Calendar.getInstance());
		voteDao.create(newVote);
	}
	
	@Transactional(readOnly = true)
	public VideoBlock findFavourites(Long userId, int startIndex, int count) 
		throws InstanceNotFoundException{
		
		userProfileDao.find(userId);
		
		List<Video> videos = favouriteDao.findFavourites(userId, startIndex, count+1);
		boolean existMoreVideos = videos.size() == (count + 1);

		if (existMoreVideos) videos.remove(videos.size() - 1);
		
		return new VideoBlock(videos, existMoreVideos);		
	}
	
	@Transactional(readOnly = true)
	public boolean isFavourite(Long userId, Long videoId){ 
		return favouriteDao.isFavourite(userId, videoId);
	}

	public Long addToFavourites(Long userId, Long videoId)
			throws InstanceNotFoundException, DuplicateInstanceException {

		UserProfile userProfile = userProfileDao.find(userId);
		if(favouriteDao.isFavourite(userProfile.getUserProfileId(), videoId))
			throw new DuplicateInstanceException(userProfile.getLoginName(), 
					Favourite.class.getName());
		Video video = videoDao.find(videoId);
		
		Favourite newFav = new Favourite(userProfile, video);
		favouriteDao.create(newFav);
		return newFav.getId();
	}
	
	public void removeFromFavourites(Long userId, Long videoId) 
			throws InstanceNotFoundException{
		
		userProfileDao.find(userId);
		favouriteDao.removeFromFavourites(userId, videoId);
		
	}
	
}
