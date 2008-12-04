package es.udc.acarballal.elmas.test.model.userservice;

import static org.junit.Assert.assertEquals;

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
	
	
	/* Tests for UserService.changePrivileges method */
	@Test
	public void testChangePrivilegesToNone() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		LoginResult newResult = 
			userService.changePrivileges(loginResult.getUserProfileId(),	
					Privileges_TYPES.NONE);
		assertEquals(newResult.getPrivileges(),Privileges_TYPES.NONE);
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
	
	/* Tests for UserService.changePrivilegesToAdmin method */
	@Test
	public void testChangePrivilegesToAdminAsAdmin_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsCompetitor_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsVoter_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsNone_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(DbUtil.getTestUserProfile()
				.getLoginName(), DbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.NONE);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
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
}
