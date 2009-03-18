/*package es.udc.acarballal.elmas.test.model.videoservice;

import java.util.Calendar;
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
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { GlobalNames.SPRING_CONFIG_FILE_LOCATION })
@Transactional
public class VideoServiceTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_VIDEO_ID = -1;
	private final long NON_EXISTENT_VIDEO_COMMENT = -1;
	
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
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsAdmin() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		userService.changePrivilegesToAdmin(userProfileId, userProfileId);
		
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
		assertTrue(video.videoId == videoId);
		assertEquals(video.title, title);
		assertEquals(video.comment, comment);
		assertEquals(video.snapshot, snapshot);
		assertEquals(video.date, date);
		
	}
	
	@Test
	public void addVideoTestPrivilegesAsCompetitor() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		userService.changePrivileges(userProfileId, Privileges_TYPES.COMPETITOR);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		
		Long videoId = videoService.addVideo(userProfileId, title, comment, snapshot, 
				original, flvVideo, rtVideo, date);
		Video video = videoService.findVideoById(videoId);
		assertEquals(video.videoId, videoId);
		assertEquals(video.title, title);
		assertEquals(video.comment, comment);
		assertEquals(video.snapshot, snapshot);
		assertEquals(video.date, date);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsVoter() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		userService.changePrivileges(userProfileId, Privileges_TYPES.VOTER);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(userProfileId, title, comment, snapshot, original, 
				flvVideo, rtVideo, date);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsNone() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		String original = "video.vob";
		String flvVideo = "video.flv";
		String rtVideo = "video.mp4";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(userProfileId, title, comment, snapshot, 
				original, flvVideo, rtVideo, date);
		
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
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteVideoPrivilegesAsAdmin()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		long videoId = DbUtil.getTestVideo().getVideoId();
		userService.changePrivilegesToAdmin(userProfileId, userProfileId);
				
		videoService.deleteVideo(videoId, userProfileId);
		videoService.findVideoById(videoId);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoPrivilegesAsCompetitor()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		long videoId = DbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, Privileges_TYPES.COMPETITOR);
				
		videoService.deleteVideo(videoId, userProfileId);
	}

	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoPrivilegesAsVoter()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		long videoId = DbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, Privileges_TYPES.VOTER);
				
		videoService.deleteVideo(videoId, userProfileId);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoPrivilegesAsNone()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = DbUtil.getTestUserProfile().getUserProfileId();
		long videoId = DbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, Privileges_TYPES.NONE);
		
		videoService.deleteVideo(videoId, userProfileId);
	}

	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoCommentAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		Long userId = loginResult.getUserProfileId();
		videoService.commentVideo(userId, userId, "", null);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoCommentAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.NONE);
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
	public void addVideoComment_removeVideoComment()
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
	public void removeVideoComment() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.deleteVideoComment(NON_EXISTENT_VIDEO_COMMENT, 
				user.getUserProfileId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void voteVideoNonExistentUser()
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			InvalidOperationException{
		
		videoService.voteVideo(NON_EXISTENT_USER_PROFILE_ID, 
				DbUtil.getTestVideo().getVideoId(), VOTE_TYPES.GOOD, 
				Calendar.getInstance());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void voteVideoAsAdmin() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.voteVideo(user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId(), VOTE_TYPES.GOOD, 
				Calendar.getInstance());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void voteVideoAsCompetitor() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		videoService.voteVideo(user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId(), VOTE_TYPES.GOOD, 
				Calendar.getInstance());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void voteVideoAsNone() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.NONE);
		videoService.voteVideo(user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId(), VOTE_TYPES.GOOD, 
				Calendar.getInstance());
	}
	
	@Test(expected = InvalidOperationException.class)
	public void voteOwnVideo() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		videoService.voteVideo(user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId(), VOTE_TYPES.GOOD, 
				Calendar.getInstance());
	}
	
	@Test
	public void voteVideo() 
			throws InstanceNotFoundException, InsufficientPrivilegesException, 
			IncorrectPasswordException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		videoService.voteVideo(user.getUserProfileId(), 
				DbUtil.getTestVideo().getVideoId(), VOTE_TYPES.GOOD, 
				Calendar.getInstance());
		
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
		VideoBlock block = videoService.findVideosByTitle(keys, startIndex, count);
		assertTrue(block.getVideos().size()==3);
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
}*/
