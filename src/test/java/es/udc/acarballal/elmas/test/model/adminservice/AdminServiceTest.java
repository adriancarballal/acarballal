package es.udc.acarballal.elmas.test.model.adminservice;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.test.model.util.DbUtil;
import es.udc.acarballal.elmas.test.util.GlobalNames;
import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.adminservice.UserCommentComplaintBlock;
import es.udc.acarballal.elmas.model.adminservice.VideoCommentComplaintBlock;
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { GlobalNames.SPRING_CONFIG_FILE_LOCATION })
@Transactional
public class AdminServiceTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_COMMENT_ID = -1;
	private final long NON_VIDEO_COMMENT_COMPLAINT_ID = -1;

	private UserService userService;
	private VideoService videoService;
	private AdminService adminService;

	@BeforeClass
	public static void populateDb() {
		DbUtil.populateDb();
	}

	@AfterClass
	public static void cleanDb() throws Exception {
		DbUtil.cleanDb();
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	@Autowired
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	@Test(expected = InstanceNotFoundException.class)
	public void deleteUserNotFoundAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long user = userService.login(DbUtil.getCommentatorProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteUserProfile(user, NON_EXISTENT_USER_PROFILE_ID);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteUserNotFoundUser() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteUserProfile(NON_EXISTENT_USER_PROFILE_ID, admin);
	}
	
	@Test
	public void deleteUserAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		Long user = userService.login(DbUtil.getCommentatorProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteUserProfile(user, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		Long user = userService.login(DbUtil.getCommentatorProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		admin = userService.changePrivileges(user, Privileges_TYPES.COMPETITOR)
					.getUserProfileId();
		adminService.deleteUserProfile(user, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		Long user = userService.login(DbUtil.getCommentatorProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		admin = userService.changePrivileges(user, Privileges_TYPES.VOTER)
					.getUserProfileId();
		adminService.deleteUserProfile(user, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		Long user = userService.login(DbUtil.getCommentatorProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		admin = userService.changePrivileges(user, Privileges_TYPES.NONE)
					.getUserProfileId();
		adminService.deleteUserProfile(user, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserDeleteAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteUserProfile(admin, admin);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteUserCommentComplaintAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(NON_EXISTENT_COMMENT_ID, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserCommentComplaintAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR)
		.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(NON_EXISTENT_COMMENT_ID, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserCommentComplaintAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.VOTER)
		.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(NON_EXISTENT_COMMENT_ID, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserCommentComplaintAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.NONE)
		.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(NON_EXISTENT_COMMENT_ID, admin);
	}
	
	@Test
	public void deleteUserCommentComplaint() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		
		Long commentId = 
			userService.commentUser(admin, user, "commnet", Calendar.getInstance());
		
		Long complaintId = userService.complaintUserComment(commentId, admin);
		
		adminService.deleteUserCommentComplaint(complaintId, admin);
	}
	
	@Test
	public void deleteUser() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(commentator, 
				Privileges_TYPES.VOTER);		
		Long commentId = userService.commentUser(commentator, admin, 
				"GOOD USER", Calendar.getInstance());
		Long complaintId = userService.complaintUserComment(commentId, admin);
		
		adminService.deleteUserCommentComplaint(complaintId, admin);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteVideoCommentComplaintAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoCommentComplaintAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR)
					.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoCommentComplaintAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.VOTER)
					.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoCommentComplaintAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.NONE)
					.getUserProfileId();
		
		adminService.deleteUserCommentComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test
	public void deleteVideoCommentComplaint() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		Long commentId = 
			videoService.commentVideo(user, videoId, "", null);
		
		
		Long complaintId = 
			videoService.complaintOfVideoComment(commentId, admin);
		
		adminService.deleteVideoCommentComplaint(complaintId, admin);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteVideoComplaintAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		adminService.deleteVideoComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoComplaintAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR)
					.getUserProfileId();
		
		adminService.deleteVideoComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoComplaintAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.VOTER)
					.getUserProfileId();
		
		adminService.deleteVideoComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteVideoComplaintAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.NONE)
					.getUserProfileId();
		
		adminService.deleteVideoComplaint(
				NON_VIDEO_COMMENT_COMPLAINT_ID, admin);
		
	}
	
	@Test
	public void deleteVideoComplaint() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		Long complaintId = 
			videoService.complaintOfVideo(videoId, admin);
		
		adminService.deleteVideoComplaint(complaintId, admin);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findFirstVideoComplaintsAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR)
					.getUserProfileId();
		
		adminService.findFirstVideoComplaints(admin);

	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findFirstVideoComplaintsAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.VOTER)
					.getUserProfileId();
		
		adminService.findFirstVideoComplaints(admin);

	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findFirstVideoComplaintsAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.NONE)
					.getUserProfileId();
		
		adminService.findFirstVideoComplaints(admin);

	}
	
	@Test
	public void findFirstVideoComplaintsZero() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		assertEquals(adminService.findFirstVideoComplaints(admin),null);

	}
	
	@Test
	public void findFirstVideoComplaintsOne() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		Long complaintId = 
			videoService.complaintOfVideo(videoId, admin);
		
		assertEquals(adminService.findFirstVideoComplaints(admin)
				.getComplaintId(),complaintId);

	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findUserCommentComplaintsAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR)
					.getUserProfileId();
		
		adminService.findUserCommentComplaints(admin, 0, 1);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findUserCommentComplaintsAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.VOTER)
					.getUserProfileId();
		
		adminService.findUserCommentComplaints(admin, 0, 1);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findUserCommentComplaintsAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.NONE)
					.getUserProfileId();
		
		adminService.findUserCommentComplaints(admin, 0, 1);
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void findUserCommentComplaintsNoUser() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		adminService.findUserCommentComplaints(NON_EXISTENT_USER_PROFILE_ID, 0, 1);
		
	}
	
	@Test
	public void findUserCommentComplaintsZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		UserCommentComplaintBlock block = 
			adminService.findUserCommentComplaints(admin, 0, 1);
		
		assertTrue(block.getUserCommentComplaints().size()==0);
		assertTrue(!block.getExistMoreUserCommentComplaints());
	}
	
	@Test
	public void findUserCommentComplaintsOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		
		Long commentId = 
			userService.commentUser(admin, user, "commnet", Calendar.getInstance());
		
		Long complaintId = userService.complaintUserComment(commentId, admin);
		
		UserCommentComplaintBlock block = 
			adminService.findUserCommentComplaints(admin, 0, 1);
		
		assertTrue(block.getUserCommentComplaints().size()==1);
		assertEquals(block.getUserCommentComplaints().get(0).getComplaintId(),complaintId);
		assertTrue(!block.getExistMoreUserCommentComplaints());
	}
	
	@Test
	public void findUserCommentComplaintsHasMore() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		
		Long commentId = 
			userService.commentUser(admin, user, "commnet", Calendar.getInstance());
		
		Long complaintId = userService.complaintUserComment(commentId, admin);
		userService.complaintUserComment(commentId, admin);
		
		UserCommentComplaintBlock block = 
			adminService.findUserCommentComplaints(admin, 0, 1);
		
		assertTrue(block.getUserCommentComplaints().size()==1);
		assertEquals(block.getUserCommentComplaints().get(0).getComplaintId(),complaintId);
		assertTrue(block.getExistMoreUserCommentComplaints());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findVideoCommentComplaintsAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR)
					.getUserProfileId();
		
		adminService.findVideoCommentComplaints(admin, 0, 1);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findVideoCommentComplaintsAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.VOTER)
					.getUserProfileId();
		
		adminService.findVideoCommentComplaints(admin, 0, 1);
		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void findVideoCommentComplaintsAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		admin = userService.changePrivileges(admin, Privileges_TYPES.NONE)
					.getUserProfileId();
		
		adminService.findVideoCommentComplaints(admin, 0, 1);
		
	}
	
	@Test
	public void findVideoCommentComplaintsZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		VideoCommentComplaintBlock block = 
			adminService.findVideoCommentComplaints(admin, 0, 1);
		
		assertTrue(block.getVideoCommentComplaints().size()==0);
		assertTrue(!block.getExistMoreVideoCommentComplaints());
	}
	
	@Test
	public void findVideoCommentComplaintsOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		Long commentId = 
			videoService.commentVideo(user, videoId, "", null);
		
		Long complaintId = 
			videoService.complaintOfVideoComment(commentId, admin);
		
		VideoCommentComplaintBlock block = 
			adminService.findVideoCommentComplaints(admin, 0, 1);
		
		assertTrue(block.getVideoCommentComplaints().size()==1);
		assertEquals(block.getVideoCommentComplaints().get(0).getComplaintId(), complaintId);
		assertTrue(!block.getExistMoreVideoCommentComplaints());
	}
	
	@Test
	public void findVideoCommentComplaintsHasMore() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		Long commentId = 
			videoService.commentVideo(user, videoId, "", null);
		
		Long complaintId = 
			videoService.complaintOfVideoComment(commentId, admin);
		
		videoService.complaintOfVideoComment(commentId, admin);
		
		VideoCommentComplaintBlock block = 
			adminService.findVideoCommentComplaints(admin, 0, 1);
		
		assertTrue(block.getVideoCommentComplaints().size()==1);
		assertEquals(block.getVideoCommentComplaints().get(0).getComplaintId(), complaintId);
		assertTrue(block.getExistMoreVideoCommentComplaints());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfUserCommentComplaintsAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR);
		
		adminService.getNumberOfUserCommentComplaints(admin);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfUserCommentComplaintsAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.VOTER);
		
		adminService.getNumberOfUserCommentComplaints(admin);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfUserCommentComplaintsAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.NONE);
		
		adminService.getNumberOfUserCommentComplaints(admin);		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void getNumberOfUserCommentComplaintsNoneUser() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		adminService.getNumberOfUserCommentComplaints(NON_EXISTENT_USER_PROFILE_ID);		
	}
	
	@Test
	public void getNumberOfUserCommentComplaintsZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		
		Long commentId = 
			userService.commentUser(admin, user, "commnet", Calendar.getInstance());
		
		userService.complaintUserComment(commentId, admin);
		
		assertEquals(adminService.getNumberOfUserCommentComplaints(admin), 1);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfVideoCommentComplaintsAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR);
		
		adminService.getNumberOfVideoCommentComplaints(admin);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfVideoCommentComplaintsAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.VOTER);
		
		adminService.getNumberOfVideoCommentComplaints(admin);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfVideoCommentComplaintsAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.NONE);
		
		adminService.getNumberOfVideoCommentComplaints(admin);		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void getNumberOfVideoCommentComplaintsNoneUser() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		adminService.getNumberOfVideoCommentComplaints(NON_EXISTENT_USER_PROFILE_ID);		
	}
	
	@Test
	public void getNumberOfVideoCommentComplaintsZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		assertTrue(adminService.getNumberOfVideoCommentComplaints(admin)==0);		
	}
	
	@Test
	public void getNumberOfVideoCommentComplaintsOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		Long commentId = 
			videoService.commentVideo(user, videoId, "", null);
		
		videoService.complaintOfVideoComment(commentId, admin);
		
		assertTrue(adminService.getNumberOfVideoCommentComplaints(admin)==1);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfVideoComplaintsAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.COMPETITOR);
		
		adminService.getNumberOfVideoComplaints(admin);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfVideoComplaintsAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.VOTER);
		
		adminService.getNumberOfVideoComplaints(admin);		
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void getNumberOfVideoComplaintsAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		userService.changePrivileges(admin, Privileges_TYPES.NONE);
		
		adminService.getNumberOfVideoComplaints(admin);		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void getNumberOfVideoComplaintsNoneUser() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		adminService.getNumberOfVideoComplaints(NON_EXISTENT_USER_PROFILE_ID);
		
	}
	
	@Test
	public void getNumberOfVideoComplaintsZero() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		assertTrue(adminService.getNumberOfVideoComplaints(admin)==0);		
	}
	
	@Test
	public void getNumberOfVideoComplaintsOne() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		Long admin = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false)
				.getUserProfileId();
		
		Long user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		userService.changePrivileges(user, Privileges_TYPES.COMPETITOR);
		
		Long videoId = DbUtil.getTestVideo().getVideoId();

		videoService.complaintOfVideo(videoId, admin);
		
		assertTrue(adminService.getNumberOfVideoComplaints(admin)==1);		
	}
}