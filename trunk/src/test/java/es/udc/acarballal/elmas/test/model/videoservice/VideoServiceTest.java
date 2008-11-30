package es.udc.acarballal.elmas.test.model.videoservice;

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

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.test.model.util.VideoDbUtil;
import es.udc.acarballal.elmas.test.util.GlobalNames;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { GlobalNames.SPRING_CONFIG_FILE_LOCATION })
@Transactional
public class VideoServiceTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_VIDEO_ID = -1;
	
	private VideoService videoService;
	private UserService userService;

	@BeforeClass
	public static void populateDb() {
		VideoDbUtil.populateDb();
	}

	@AfterClass
	public static void cleanDb() throws Exception {
		VideoDbUtil.cleanDb();
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
		long videoId = VideoDbUtil.getTestVideo().getVideoId();
		Video testResultVideo = videoService.findVideoById(videoId);
		assertEquals(VideoDbUtil.getTestVideo(), testResultVideo);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void findVideoTestWithNonInstance() throws InstanceNotFoundException {
		videoService.findVideoById(NON_EXISTENT_VIDEO_ID);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsAdmin() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.ADMIN);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		
		Long videoId = videoService.addVideo(userProfileId, title, comment, snapshot, date);
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
		
		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.COMPETITOR);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		
		Long videoId = videoService.addVideo(userProfileId, title, comment, snapshot, date);
		Video video = videoService.findVideoById(videoId);
		assertTrue(video.videoId == videoId);
		assertEquals(video.title, title);
		assertEquals(video.comment, comment);
		assertEquals(video.snapshot, snapshot);
		assertEquals(video.date, date);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsVoter() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.VOTER);
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(userProfileId, title, comment, snapshot, date);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addVideoTestPrivilegesAsNone() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(userProfileId, title, comment, snapshot, date);
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addVideoTestWithNonExistentUser() 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		long userId = NON_EXISTENT_USER_PROFILE_ID;
		String title = "VideoExample";
		String comment = "CommentExample";
		String snapshot = "-home-data-snapshots-001.jpg";
		Calendar date = Calendar.getInstance();
		
		videoService.addVideo(userId, title, comment, snapshot, date);
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteVideoPrivilegesAsAdmin()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		long videoId = VideoDbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.ADMIN);
				
		videoService.deleteVideo(videoId, userProfileId);
		videoService.findVideoById(videoId);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoPrivilegesAsCompetitor()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		long videoId = VideoDbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.COMPETITOR);
				
		videoService.deleteVideo(videoId, userProfileId);
	}

	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoPrivilegesAsVoter()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		long videoId = VideoDbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.VOTER);
				
		videoService.deleteVideo(videoId, userProfileId);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoPrivilegesAsNone()
			throws InstanceNotFoundException, InsufficientPrivilegesException {

		long userProfileId = VideoDbUtil.getTestUserProfile().getUserProfileId();
		long videoId = VideoDbUtil.getTestVideo().getVideoId();
		userService.changePrivileges(userProfileId, userProfileId, Privileges_TYPES.NONE);
		
		videoService.deleteVideo(videoId, userProfileId);
	}


}
