package es.udc.acarballal.elmas.test.model.userservice;

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
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.LoginResult;
import es.udc.acarballal.elmas.model.userservice.MessageBlock;
import es.udc.acarballal.elmas.model.userservice.UserCommentBlock;
import es.udc.acarballal.elmas.model.userservice.UserProfileDetails;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { GlobalNames.SPRING_CONFIG_FILE_LOCATION })
@Transactional
public class UserServiceTest {

	private final String NON_EXISTENT_LOGIN_NAME = "nonExistentLoginName";
	private final String INCORRECT_CLEAR_PASSWORD = "incorrectClearPassword";

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_COMMENT_ID = -1;
	private final long NON_EXISTENT_MESSAGE_ID = -1;

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
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Test
	public void testRegisterUser() throws DuplicateInstanceException,
			InstanceNotFoundException {
		String loginName = "user2";
		String clearPassword = "userPassword";
		String firstName = "name";
		String lastName = "lastName";
		String email = "user2@udc.es";

		UserProfileDetails userProfileDetails = new UserProfileDetails(
				firstName, lastName, email);
		Long userProfileId = userService.registerUser(loginName, clearPassword,
				userProfileDetails);
		UserProfileDetails userProfileDetails2 = userService
				.findUserProfileDetails(userProfileId);

		assertEquals(userProfileDetails, userProfileDetails2);
	}

	@Test(expected = DuplicateInstanceException.class)
	public void testRegisterDuplicatedUser() throws DuplicateInstanceException {
		userService.registerUser(DbUtil.getTestUserProfile().getLoginName(),
				"clearPassword", new UserProfileDetails("name", "lastName",
						"user1@udc.es"));
	}

	@Test
	public void testLoginClearPassword() throws IncorrectPasswordException,
			InstanceNotFoundException {
		LoginResult loginResult = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		assertEquals(loginResult.getFirstName(), DbUtil.getTestUserProfile()
				.getFirstName());
		assertEquals(loginResult.getEncryptedPassword(), DbUtil
				.getTestUserProfile().getEncryptedPassword());
	}

	@Test
	public void testLoginEncryptedPassword() throws IncorrectPasswordException,
			InstanceNotFoundException {
		LoginResult loginResult = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestUserProfile()
				.getEncryptedPassword(), true);
		assertEquals(loginResult.getFirstName(), DbUtil.getTestUserProfile()
				.getFirstName());
		assertEquals(loginResult.getEncryptedPassword(), DbUtil
				.getTestUserProfile().getEncryptedPassword());
	}

	@Test(expected = IncorrectPasswordException.class)
	public void testLoginIncorrectPasword() throws IncorrectPasswordException,
			InstanceNotFoundException {
		userService.login(DbUtil.getTestUserProfile().getLoginName(),
				INCORRECT_CLEAR_PASSWORD, false);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testLoginWithNonExistentUser()
			throws IncorrectPasswordException, InstanceNotFoundException {
		userService.login(NON_EXISTENT_LOGIN_NAME, DbUtil
				.getTestClearPassword(), false);
	}

	@Test
	public void testUpdate() throws InstanceNotFoundException,
			IncorrectPasswordException {
		LoginResult loginResult = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);

		UserProfileDetails userProfileDetails = new UserProfileDetails("name2",
				"lastName2", "user2@udc.es");
		userService.updateUserProfileDetails(loginResult.getUserProfileId(),
				userProfileDetails);

		UserProfileDetails userProfileDetails2 = userService
				.findUserProfileDetails(loginResult.getUserProfileId());
		assertEquals(userProfileDetails, userProfileDetails2);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateWithNonExistentUser()
			throws InstanceNotFoundException {
		UserProfileDetails userProfileDetails = new UserProfileDetails("name2",
				"lastName2", "user2@udc.es");
		userService.updateUserProfileDetails(NON_EXISTENT_USER_PROFILE_ID,
				userProfileDetails);
	}

	@Test
	public void testChangePassword() throws InstanceNotFoundException,
			IncorrectPasswordException {
		LoginResult loginResult = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);

		String newClearPassword = "newClearPassword";
		userService.changePassword(loginResult.getUserProfileId(), DbUtil
				.getTestClearPassword(), newClearPassword);
		userService.login(DbUtil.getTestUserProfile().getLoginName(),
				newClearPassword, false);
	}

	@Test(expected = IncorrectPasswordException.class)
	public void testChangePasswordWithIncorrectPassword()
			throws InstanceNotFoundException, IncorrectPasswordException {
		LoginResult loginResult = userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);

		String newClearPassword = "newClearPassword";
		userService.changePassword(loginResult.getUserProfileId(),
				INCORRECT_CLEAR_PASSWORD, newClearPassword);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testChangePasswordWithNonExistentUser()
			throws InstanceNotFoundException, IncorrectPasswordException {
		String newClearPassword = "newClearPassword";
		userService.changePassword(NON_EXISTENT_USER_PROFILE_ID,
				newClearPassword, newClearPassword);
	}
	
	

	@Test
	public void testChangePrivilegesToNoneAsAdmin() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		LoginResult newResult = 
			userService.changePrivileges(loginResult.getUserProfileId(),	
					Privileges_TYPES.NONE);
		assertEquals(newResult.getPrivileges(),Privileges_TYPES.NONE);
	}
	
	@Test
	public void testChangePrivilegesToVoterAsAdmin() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		LoginResult newResult = 
			userService.changePrivileges(loginResult.getUserProfileId(),	
					Privileges_TYPES.VOTER);
		assertEquals(newResult.getPrivileges(),Privileges_TYPES.VOTER);
	}
	
	@Test
	public void testChangePrivilegesToCompetitorAsAdmin() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		LoginResult newResult = 
			userService.changePrivileges(loginResult.getUserProfileId(),	
					Privileges_TYPES.COMPETITOR);
		assertEquals(newResult.getPrivileges(),Privileges_TYPES.COMPETITOR);
	}
	
	
	
	
	
	
	
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsAdmin() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsCompetitor() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsVoter() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsNone() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.NONE);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testChangePrivilegesWithNonExistentUser() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		userService.changePrivileges(NON_EXISTENT_USER_PROFILE_ID, Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InvalidOperationException.class)
	public void addUserCommentAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId, userId, "", null);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addUserCommentAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.NONE);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId, userId, "", null);
	}

	@Test(expected = InvalidOperationException.class)
	public void addUserCommentToYourselfAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId, userId, "", null);
	}
	
	@Test(expected = InvalidOperationException.class)
	public void addUserCommentToYourselfAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId, userId, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addUserCommentNoCommentatorFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(this.NON_EXISTENT_USER_PROFILE_ID, userId, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addUserCommentNoCommentatedFoundAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.NONE);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId,this.NON_EXISTENT_USER_PROFILE_ID, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addUserCommentNoCommentatedFoundAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId,this.NON_EXISTENT_USER_PROFILE_ID, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addUserCommentNoCommentatedFoundAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId,this.NON_EXISTENT_USER_PROFILE_ID, "", null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void addUserCommentNoCommentatedFoundAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		LoginResult loginResult = 
			userService.login(DbUtil.getTestUserProfile()
			.getLoginName(), DbUtil.getTestClearPassword(), false);
		Long userId = loginResult.getUserProfileId();
		userService.commentUser(userId,this.NON_EXISTENT_USER_PROFILE_ID, "", null);
	}
	
	@Test
	public void addUserComment_removeUserComment()
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
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void deleteUserCommentNotFound() 
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
		
		userService.deleteUserComment(NON_EXISTENT_COMMENT_ID, user.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserCommentAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long commentId = userService.commentUser(user.getUserProfileId(), 
				commentator.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult deleter = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);		
		userService.deleteUserComment(commentId, deleter.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserCommentAsCompetitor() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long commentId = userService.commentUser(user.getUserProfileId(), 
				commentator.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult deleter = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);		
		userService.deleteUserComment(commentId, deleter.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void deleteUserCommentAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long commentId = userService.commentUser(user.getUserProfileId(), 
				commentator.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult deleter = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.NONE);		
		userService.deleteUserComment(commentId, deleter.getUserProfileId());
	}
	
	@Test
	public void deleteUserCommentAsAdmin() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		LoginResult user = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult commentator = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void addComplaintUserCommentAsNone() 
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
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult complainer = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.NONE);
		
		userService.complaintUserComment(commentId, complainer.getUserProfileId());
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void addComplaintUserCommentAsAdmin() 
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
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult complainer = user;
		
		userService.complaintUserComment(commentId, complainer.getUserProfileId());
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void addComplaintUserCommentAsCompetitor() 
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
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult complainer = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		
		userService.complaintUserComment(commentId, complainer.getUserProfileId());
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void addComplaintUserCommentAsVoter() 
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
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult complainer = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		userService.complaintUserComment(commentId, complainer.getUserProfileId());
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
		
	}
	
	@Test
	public void isUserCommentComplaintedTrue() 
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
		
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult complainer = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		userService.complaintUserComment(commentId, complainer.getUserProfileId());
		
		assertTrue(userService.isUserCommentComplaintedBy(
				user.getUserProfileId(), commentId));
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void isUserCommentComplaintedFalse() 
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
		
		Long commentId = userService.commentUser(commentator.getUserProfileId(), 
				user.getUserProfileId(), "GOOD USER", Calendar.getInstance());
		
		LoginResult complainer = userService.changePrivileges(user.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		
		userService.complaintUserComment(commentId, complainer.getUserProfileId());
		
		assertFalse(userService.isUserCommentComplaintedBy(
				commentator.getUserProfileId(), commentId));
		
		userService.deleteUserComment(commentId, user.getUserProfileId());
	}
	
	@Test
	public void findUserCommentsByCommentator(){
		int startIndex = 0;
		int count = 10;
		UserCommentBlock comments = 
			userService.findUserCommentsByCommentator
			(DbUtil.getTestUserProfile().getUserProfileId(), 
				startIndex, count);
		assertTrue(comments.getUserComments().size()==2);
	}
	
	@Test
	public void findUserCommentsByCommented(){
		int startIndex = 0;
		int count = 10;
		UserCommentBlock comments = 
			userService.findUserCommentsByCommented
			(DbUtil.getTestUserProfile().getUserProfileId(), 
				startIndex, count);
		assertTrue(comments.getUserComments().size()==1);
		assertEquals(comments.getUserComments().get(0), DbUtil.getUserComment1());
	}
	
	@Test
	public void findUserCommentsByCommentatorNoNUser(){
		int startIndex = 0;
		int count = 10;
		UserCommentBlock comments = 
			userService.findUserCommentsByCommentator
			(NON_EXISTENT_USER_PROFILE_ID, startIndex, count);
		assertTrue(comments.getUserComments().size()==0);
	}
	
	@Test
	public void findUserCommentsByCommentedNoNUser(){
		int startIndex = 0;
		int count = 10;
		UserCommentBlock comments = 
			userService.findUserCommentsByCommented
			(NON_EXISTENT_USER_PROFILE_ID, startIndex, count);
		assertTrue(comments.getUserComments().size()==0);
	}
	
	@Test
	public void sendMessage() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
	}
	
	@Test
	public void removeMessage() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long id = userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
		
		userService.removeMessage(id, user.getUserProfileId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void sendMessageNoSender() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		userService.sendMessage(admin.getUserProfileId(), 
				NON_EXISTENT_USER_PROFILE_ID, "message");
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void sendMessageNoReceiver() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		userService.sendMessage(NON_EXISTENT_USER_PROFILE_ID, 
				user.getUserProfileId(), "message");
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void RemoveMessageAsSender() 
			throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long id = userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
		
		userService.removeMessage(id, admin.getUserProfileId());
	}
	
	@Test
	public void RemoveMessageAsReceiver() 
			throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		Long id = userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
		
		userService.removeMessage(id, user.getUserProfileId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void removeMessageNotFound() 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		userService.removeMessage(NON_EXISTENT_MESSAGE_ID, user.getUserProfileId());
	}
	
	@Test
	public void getTotalInboxZero() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		assertEquals(userService.getInBoxTotal(user.getUserProfileId()), 0);
		
	}
	
	@Test
	public void getTotalInboxOne() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
		
		assertEquals(userService.getInBoxTotal(user.getUserProfileId()), 1);
		
	}
	
	@Test
	public void findUserInboxZero() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		MessageBlock block = userService.findUserInBox(user.getUserProfileId(), 0, 10); 
		assertEquals(block.getmessages().size(),0);
		
	}
	
	@Test
	public void findUserInboxOne() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
		
		MessageBlock block = userService.findUserInBox(user.getUserProfileId(), 0, 10); 
		assertEquals(block.getmessages().size(),1);
		
	}
	
	@Test
	public void findUserInboxHasNext() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		LoginResult user = 
			userService.login(DbUtil.getCommentatorProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		for(int i = 1;i<12;i++){
			userService.sendMessage(admin.getUserProfileId(), 
				user.getUserProfileId(), "message");
		}
		
		MessageBlock block = userService.findUserInBox(user.getUserProfileId(), 0, 10); 
		assertTrue(block.getExistMoreMessages());
		
	}
}