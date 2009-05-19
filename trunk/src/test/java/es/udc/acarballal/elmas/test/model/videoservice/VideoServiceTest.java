package es.udc.acarballal.elmas.test.model.videoservice;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.LoginResult;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoCommentBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.acarballal.elmas.test.model.util.DbUtil;
import es.udc.acarballal.elmas.test.util.GlobalNames;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { GlobalNames.SPRING_CONFIG_FILE_LOCATION })
@Transactional
public class VideoServiceTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_VIDEO_ID = -1;
	private final long NON_EXISTENT_VIDEO_COMMENT = -1;
	private final long NON_EXISTENT_FAVOURITE_ID = -1;
	
	private VideoService videoService;
	private UserService userService;

	@BeforeClass
	public static void populateDb() {
		DbUtil.populateDb();
	}

	@AfterClass
	public static void cleanDb() throws Exception {
		DbUtil.cleanDb();
	}
	
	@Autowired
	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Test
	public void findVideoTest() throws InstanceNotFoundException {
		long videoId = DbUtil.getTestVideo().getVideoId();
		Video testResultVideo = videoService.findVideoById(videoId);
		assertEquals(DbUtil.getTestVideo(), testResultVideo);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void findVideoTestWithNonInstance() throws InstanceNotFoundException {
		videoService.findVideoById(NON_EXISTENT_VIDEO_ID);
	}
	
	@Test
	public void addVideoTestPrivilegesAsAdmin() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		Calendar date = Calendar.getInstance();
		
		Long videoId = videoService.addVideo(userProfileId, title, comment, snapshot, 
				original, flvVideo, rtVideo, date);
		Video video = videoService.findVideoById(videoId);
		assertTrue(video.getVideoId() == videoId);
		assertEquals(video.getTitle(), title);
		assertEquals(video.getComment(), comment);
		assertEquals(video.getSnapshot(), snapshot);
		assertEquals(video.getDate(), date);
		
	}
	
	@Test
	public void addVideoTestPrivilegesAsCompetitor() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		LoginResult result = userService.changePrivileges(userProfileId, Privileges_TYPES.COMPETITOR);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		
		Long videoId = videoService.addVideo(result.getUserProfileId(), 
				title, comment, snapshot, original, flvVideo, rtVideo, date);
		Video video = videoService.findVideoById(videoId);
		assertTrue(video.getVideoId() == videoId);
		assertEquals(video.getTitle(), title);
		assertEquals(video.getComment(), comment);
		assertEquals(video.getSnapshot(), snapshot);
		assertEquals(video.getDate(), date);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsVoter() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		LoginResult result = userService.changePrivileges(userProfileId, Privileges_TYPES.VOTER);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(result.getUserProfileId(), title, comment, 
				snapshot, original,	flvVideo, rtVideo, date);
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addVideoTestWithNonExistentUser() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userId = NON_EXISTENT_USER_PROFILE_ID;
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(userId, title, comment, snapshot, 
				original, flvVideo, rtVideo, date);
		
	}
	
	public void addVideoCommentAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InvalidOperationException{
		
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		Long userId = loginResult.getUserProfileId();
		videoService.commentVideo(userId, userId, "", null);
	}
	
	@Test(expected = InvalidOperationException.class)
	public void addVideoCommentToOwnVideoAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		Long userId = loginResult.getUserProfileId();
		Long videoId = DbUtil.getTestVideo().getVideoId();
		videoService.commentVideo(userId, videoId, "", null);
	}
	
	@Test(expected = InvalidOperationException.class)
	public void addVideoCommentToYourselfAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long userId = loginResult.getUserProfileId();
		Long videoId = DbUtil.getTestVideo().getVideoId();
		videoService.commentVideo(userId, videoId, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addVideoCommentNoCommentatorFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long videoId = DbUtil.getTestVideo().getVideoId();
		videoService.commentVideo(this.NON_EXISTENT_USER_PROFILE_ID, videoId, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addVideoCommentNoVideoFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long userId = loginResult.getUserProfileId();
		videoService.commentVideo(userId,NON_EXISTENT_VIDEO_COMMENT, "", null);
	}
	
	@Test
	public void addVideoComment_removeVideoCommentAsOwner()
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());
		videoService.deleteVideoComment(commentId, user.getUserProfileId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void removeVideoCommentNotFound() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.deleteVideoComment(NON_EXISTENT_VIDEO_COMMENT, 
				user.getUserProfileId());
	}
	
	@Test
	public void removeVideoCommentAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());
		videoService.deleteVideoComment(commentId, user.getUserProfileId());
				
	}
	
	@Test
	public void removeVideoCommentAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		videoService.deleteVideoComment(commentId, user.getUserProfileId());
				
	}
	
	@Test
	public void removeVideoCommentAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());
		videoService.deleteVideoComment(commentId, user.getUserProfileId());
				
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void voteVideoNonExistentUser()
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			InvalidOperationException, VideoAlreadyVotedException{
		
		videoService.voteVideo(VOTE_TYPES.GOOD, NON_EXISTENT_USER_PROFILE_ID, 
				DbUtil.getTestVideo().getVideoId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void voteVideoAsAdmin() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException, VideoAlreadyVotedException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		LoginResult result = userService.changePrivileges(user.getUserProfileId(),
				Privileges_TYPES.ADMIN);
		videoService.voteVideo( VOTE_TYPES.GOOD, result.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
	}
	
	@Test
	public void voteVideoAsCompetitor() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException, VideoAlreadyVotedException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult result = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		videoService.voteVideo(VOTE_TYPES.GOOD, result.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
	}
	
	@Test
	public void voteVideoAsVoter() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException, VideoAlreadyVotedException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult result = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		videoService.voteVideo(VOTE_TYPES.GOOD, result.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
	}
	
	@Test(expected = VideoAlreadyVotedException.class)
	public void voteVideoAlreadyVoted() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException, VideoAlreadyVotedException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult result = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		videoService.voteVideo(VOTE_TYPES.GOOD, result.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
		videoService.voteVideo(VOTE_TYPES.GOOD, result.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
	}
	
	@Test(expected = InvalidOperationException.class)
	public void voteOwnVideo() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException, VideoAlreadyVotedException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		videoService.voteVideo(VOTE_TYPES.GOOD, user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
	}
	
	@Test
	public void voteVideo() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException, VideoAlreadyVotedException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.voteVideo(VOTE_TYPES.GOOD, user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
		
	}
	
	@Test
	public void getRemainingVotesAll() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		assertEquals(videoService.getNumberVotesRemaining(
				user.getUserProfileId()), 10);
	}
	
	@Test
	public void getRemainingVotes() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, VideoAlreadyVotedException, 
			InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.voteVideo(VOTE_TYPES.GOOD, user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId());
		assertEquals(videoService.getNumberVotesRemaining(
				user.getUserProfileId()), 9);
	}
	
	@Test
	public void findRandomVotableVideo() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		assertTrue(
				videoService.findRandomVotableVideo(user.getUserProfileId(), 100)!=null);
	}
	
	@Test
	public void isVotableTrue() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		assertTrue(!videoService.isVideoVotable(video.getVideoId(), 
				user.getUserProfileId()));
	}
	
	@Test
	public void isVotableFalse() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, VideoAlreadyVotedException, 
			InvalidOperationException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		videoService.voteVideo(VOTE_TYPES.GOOD, user.getUserProfileId(), 
				video.getVideoId());
		assertTrue(videoService.isVideoVotable(video.getVideoId(), 
				user.getUserProfileId()));
	}
	
	@Test
	public void findMostVotedNone(){

		List<Video> videos = videoService.findMostVoted(10);
		assertTrue(videos.size()==0);
	}
	
	@Test
	public void findMostVotedOne() 
		throws InstanceNotFoundException, IncorrectPasswordException, 
		InsufficientPrivilegesException, VideoAlreadyVotedException, 
		InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		videoService.voteVideo(VOTE_TYPES.GOOD, user.getUserProfileId(), 
				video.getVideoId());
		List<Video> videos = videoService.findMostVoted(10);
		assertTrue(videos.size()==1);
		assertTrue(videos.get(0).equals(video));
	}
	
	@Test
	public void findMostVotedWeekNone(){
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.AM_PM, Calendar.AM);
		startDate.set(Calendar.MILLISECOND, 0);
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.AM_PM, Calendar.PM);
		endDate.set(Calendar.MILLISECOND, 999);
		List<Video> videos = 
			videoService.findMostVoted(startDate, endDate, 10);
		assertEquals(videos.size(), 0);
	}
	
	@Test
	public void findMostVotedWeekOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, VideoAlreadyVotedException, 
			InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		videoService.voteVideo(VOTE_TYPES.GOOD, user.getUserProfileId(), 
				video.getVideoId());
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.AM_PM, Calendar.AM);
		startDate.set(Calendar.MILLISECOND, 0);
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.AM_PM, Calendar.PM);
		endDate.set(Calendar.MILLISECOND, 999);
		List<Video> videos = 
			videoService.findMostVoted(startDate, endDate, 10);
		assertEquals(videos.size(), 1);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void complaintVideoNotFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.complaintOfVideo(NON_EXISTENT_VIDEO_ID, 
				user.getUserProfileId());
	}
	
	@Test
	public void complaintVideoAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		videoService.complaintOfVideo(video.getVideoId(), 
				user.getUserProfileId());
	}
	
	@Test
	public void complaintVideoAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		LoginResult result = userService.changePrivileges(
				user.getUserProfileId(), Privileges_TYPES.COMPETITOR);
		videoService.complaintOfVideo(video.getVideoId(), 
				result.getUserProfileId());
	}
	
	@Test
	public void complaintVideoAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		LoginResult result = userService.changePrivileges(
				user.getUserProfileId(), Privileges_TYPES.VOTER);
		videoService.complaintOfVideo(video.getVideoId(), 
				result.getUserProfileId());
	}
	
	@Test
	public void isVideoComplaintedTrue() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		videoService.complaintOfVideo(video.getVideoId(), 
				user.getUserProfileId());
		
		assertTrue(videoService.isComplaintedBy(
				user.getUserProfileId(), video.getVideoId()));
	}
	
	@Test
	public void isVideoComplaintedFalse() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Video video = DbUtil.getTestVideo();
		
		assertTrue(!videoService.isComplaintedBy(
				user.getUserProfileId(), video.getVideoId()));
	}
	
	@Test
	public void addComplaintVideoCommentAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());

		user = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		videoService.complaintOfVideoComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void addComplaintVideoCommentAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());

		user = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		
		videoService.complaintOfVideoComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void addComplaintVideoCommentAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());

		videoService.complaintOfVideoComment(commentId, user.getUserProfileId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addComplaintVideoCommentNotFoundComment() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);

		user = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		videoService.complaintOfVideoComment(NON_EXISTENT_VIDEO_COMMENT, 
				user.getUserProfileId());
	}

	@Test
	public void isVideoCommentComplaintedTrue() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());

		videoService.complaintOfVideoComment(commentId, user.getUserProfileId());
		
		assertTrue(videoService.isVideoCommentComplaintedBy(
				user.getUserProfileId(), commentId));
	}

	@Test
	public void isVideoCommentComplaintedFalse() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(commentator.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		Long commentId = videoService.commentVideo(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD Video", Calendar.getInstance());

		videoService.complaintOfVideoComment(commentId, user.getUserProfileId());
		
		assertTrue(!videoService.isVideoCommentComplaintedBy(
				commentator.getUserProfileId(), commentId));
	}
	
	
	
	
	
	
	
	
	
		
	@Test
	public void findVideoByTitleUniqueKey(){
		
		String keys = DbUtil.getTestVideo().getTitle();
		int startIndex = 0;
		int count = 10;
		VideoBlock block = videoService.findVideosByTitle(keys, startIndex, count);
		assertTrue(block.getVideos().size()==1);
		assertEquals(block.getVideos().get(0), DbUtil.getTestVideo());
	}
	
	@Test
	public void findVideoByTitleMultiKey(){
		
		String keys = DbUtil.getTestVideoMultiKey().getTitle();
		int startIndex = 0;
		int count = 10;
		VideoBlock block = videoService.findVideosByTitle(keys, startIndex, count);
		assertTrue(block.getVideos().size()==1);
		assertEquals(block.getVideos().get(0), DbUtil.getTestVideoMultiKey());
	}
	
	@Test
	public void findVideoByTitle(){
		
		String keys = "xamp";
		int startIndex = 0;
		int count = 10;
		VideoBlock block = videoService.findVideosByTitle(keys, startIndex, count);
		assertTrue(block.getVideos().size()==3);
	}

	@Test
	public void findVideoByTitleNoKey(){
		
		String keys = "";
		int startIndex = 0;
		int count = 10;
		videoService.findVideosByTitle(keys, startIndex, count);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void findVideoByIdNotFound() 
			throws InstanceNotFoundException{
		
		videoService.findVideoById(NON_EXISTENT_VIDEO_ID);
	}
	
	@Test
	public void findVideoById() 
			throws InstanceNotFoundException{
		
		Video video = 
			videoService.findVideoById(DbUtil.getTestVideo().getVideoId());
		assertTrue(video.equals(DbUtil.getTestVideo()));
	}
	
	@Test 
	public void findVideoCommentByVideoId(){
		int startIndex = 0;
		int count = 10;
		VideoCommentBlock block = 
			videoService.findVideoCommentsByVideoId(DbUtil.getTestVideo().getVideoId(), 
					startIndex, count);
		assertTrue(block.getUserComments().size()==1);
		assertEquals(block.getUserComments().get(0), DbUtil.getVideoComment());
	}

	@Test 
	public void findVideoCommentByVideoIdNonVideo(){
		int startIndex = 0;
		int count = 10;
		VideoCommentBlock block = 
			videoService.findVideoCommentsByVideoId(NON_EXISTENT_VIDEO_ID, 
					startIndex, count);
		assertTrue(block.getUserComments().size()==0);
	}
	
	@Test
	public void findVideosByUserZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		
		VideoBlock block = 
			videoService.findVideosByUser(user.getUserProfileId(), 0, 5);
		
		assertTrue(block.getVideos().size()==0);
		assertTrue(!block.getExistMoreVideos());
	}
	
	@Test
	public void findVideosByUserOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		
		Long videoId = videoService.addVideo(user.getUserProfileId(), title, comment, 
				snapshot, original, flvVideo, rtVideo, date);		
		Video video = videoService.findVideoById(videoId);
		
		VideoBlock block = 
			videoService.findVideosByUser(user.getUserProfileId(), 0, 5);
		
		assertTrue(block.getVideos().size()==1);
		assertTrue(block.getVideos().get(0).equals(video));
		assertTrue(!block.getExistMoreVideos());
	}
	
	@Test
	public void findVideosByUserHasMore() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		
		for(int i=0; i<7;i++)
			videoService.addVideo(user.getUserProfileId(), title, comment, 
				snapshot, original, flvVideo, rtVideo, date);		
				
		VideoBlock block = 
			videoService.findVideosByUser(user.getUserProfileId(), 0, 5);
		
		assertTrue(block.getVideos().size()==5);
		assertTrue(block.getExistMoreVideos());
	}
	
	@Test
	public void findVideoCommentsByUserZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		VideoCommentBlock block = 
			videoService.findVideoCommentsByUserId(user.getUserProfileId(), 0, 5);
		
		assertTrue(block.getUserComments().size()==0);
		assertTrue(!block.getExistMoreUserComments());
	}
	
	@Test
	public void findVideoCommentsByUserOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		VideoCommentBlock block = 
			videoService.findVideoCommentsByUserId(user.getUserProfileId(), 0, 5);
		
		assertTrue(block.getUserComments().size()==1);
		assertTrue(!block.getExistMoreUserComments());
	}
	
	@Test
	public void findVideoCommentsByUserHasMore() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult user = 
			userService.login(
					DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		Long video = DbUtil.getTestVideo().getVideoId();
		
		for(int i=0;i<7;i++)
			videoService.commentVideo(user.getUserProfileId(), 
					video, "", null);
		
		VideoCommentBlock block = 
			videoService.findVideoCommentsByUserId(user.getUserProfileId(), 0, 5);
		
		assertTrue(block.getUserComments().size()==5);
		assertTrue(block.getExistMoreUserComments());
	}
	
	@Test
	public void addToFavouritesAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		LoginResult user = 
			userService.login(
					DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		videoService.addToFavourites(user.getUserProfileId(), videoId);
	}
	
	@Test
	public void addToFavouritesAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		LoginResult user = 
			userService.login(
					DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		videoService.addToFavourites(user.getUserProfileId(), videoId);
	}
	
	@Test
	public void addToFavouritesAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		LoginResult user = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		videoService.addToFavourites(user.getUserProfileId(), videoId);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addToFavouritesNotFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		LoginResult user = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
				
		videoService.addToFavourites(user.getUserProfileId(),
				NON_EXISTENT_VIDEO_ID);
	}
	
	@Test(expected = DuplicateInstanceException.class)
	public void addToFavouritesDuplicate() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		LoginResult user = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		videoService.addToFavourites(user.getUserProfileId(), videoId);
		videoService.addToFavourites(user.getUserProfileId(), videoId);
	}
	
	@Test
	public void removeFromFavouritesNotFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		LoginResult user = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		videoService.removeFromFavourites(user.getUserProfileId(), 
				NON_EXISTENT_FAVOURITE_ID);
	}
	
	@Test
	public void removeFromFavouritesAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		LoginResult user = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		Long fav = 
			videoService.addToFavourites(
					user.getUserProfileId(), videoId);
		
		videoService.removeFromFavourites(user.getUserProfileId(), fav);
	}
	
	@Test
	public void removeFromFavouritesAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		LoginResult user = 
			userService.login(
					DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		Long fav = 
			videoService.addToFavourites(
					user.getUserProfileId(), videoId);
		
		videoService.removeFromFavourites(user.getUserProfileId(), fav);
	}
	
	@Test
	public void removeFromFavouritesAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		LoginResult user = 
			userService.login(
					DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		Long fav = 
			videoService.addToFavourites(
					user.getUserProfileId(), videoId);
		
		videoService.removeFromFavourites(user.getUserProfileId(), fav);
	}
	
	@Test
	public void isFavouriteTrue()	
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		videoService.addToFavourites(userId, videoId);
		assertTrue(videoService.isFavourite(userId, videoId));
	}
	
	@Test
	public void isFavouriteFalse()	
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		Long videoId = DbUtil.getTestVideo().getVideoId();
		
		assertTrue(!videoService.isFavourite(userId, videoId));
	}
	
	@Test
	public void findFavouritesAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		
		videoService.findFavourites(userId, 0, 10);
		
	}
	
	@Test
	public void findFavouritesAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		userService.changePrivileges(userId, Privileges_TYPES.COMPETITOR);		
		videoService.findFavourites(userId, 0, 10);
		
	}
	
	@Test
	public void findFavouritesAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		userService.changePrivileges(userId, Privileges_TYPES.VOTER);		
		videoService.findFavourites(userId, 0, 10);
		
	}
	
	@Test
	public void findFavouritesZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		
		VideoBlock block = videoService.findFavourites(userId, 0, 10);
		assertTrue(block.getVideos().size()==0);
		assertTrue(!block.getExistMoreVideos());
	}
	
	@Test
	public void findFavouritesOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, DuplicateInstanceException{
		
		Long userId = 
			userService.login(
					DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).
					getUserProfileId();
		Long videoId = DbUtil.getTestVideo().getVideoId();		
		videoService.addToFavourites(userId, videoId);
		
		VideoBlock block = videoService.findFavourites(userId, 0, 10);
		assertTrue(block.getVideos().size()==1);
		assertTrue(!block.getExistMoreVideos());
	}
}
