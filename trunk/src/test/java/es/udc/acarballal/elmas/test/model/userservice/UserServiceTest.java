package es.udc.acarballal.elmas.test.model.userservice;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.test.model.util.UserProfileDbUtil;
import es.udc.acarballal.elmas.test.util.GlobalNames;
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
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
		UserProfileDbUtil.populateDb();
	}

	@AfterClass
	public static void cleanDb() throws Exception {
		UserProfileDbUtil.cleanDb();
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
		userService.registerUser(UserProfileDbUtil.getTestUserProfile().getLoginName(),
				"clearPassword", new UserProfileDetails("name", "lastName",
						"user1@udc.es"));
	}

	@Test
	public void testLoginClearPassword() throws IncorrectPasswordException,
			InstanceNotFoundException {
		LoginResult loginResult = userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		assertEquals(loginResult.getFirstName(), UserProfileDbUtil.getTestUserProfile()
				.getFirstName());
		assertEquals(loginResult.getEncryptedPassword(), UserProfileDbUtil
				.getTestUserProfile().getEncryptedPassword());
	}

	@Test
	public void testLoginEncryptedPassword() throws IncorrectPasswordException,
			InstanceNotFoundException {
		LoginResult loginResult = userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestUserProfile()
				.getEncryptedPassword(), true);
		assertEquals(loginResult.getFirstName(), UserProfileDbUtil.getTestUserProfile()
				.getFirstName());
		assertEquals(loginResult.getEncryptedPassword(), UserProfileDbUtil
				.getTestUserProfile().getEncryptedPassword());
	}

	@Test(expected = IncorrectPasswordException.class)
	public void testLoginIncorrectPasword() throws IncorrectPasswordException,
			InstanceNotFoundException {
		userService.login(UserProfileDbUtil.getTestUserProfile().getLoginName(),
				INCORRECT_CLEAR_PASSWORD, false);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testLoginWithNonExistentUser()
			throws IncorrectPasswordException, InstanceNotFoundException {
		userService.login(NON_EXISTENT_LOGIN_NAME, UserProfileDbUtil
				.getTestClearPassword(), false);
	}

	@Test
	public void testUpdate() throws InstanceNotFoundException,
			IncorrectPasswordException {
		LoginResult loginResult = userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);

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
		LoginResult loginResult = userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);

		String newClearPassword = "newClearPassword";
		userService.changePassword(loginResult.getUserProfileId(), UserProfileDbUtil
				.getTestClearPassword(), newClearPassword);
		userService.login(UserProfileDbUtil.getTestUserProfile().getLoginName(),
				newClearPassword, false);
	}

	@Test(expected = IncorrectPasswordException.class)
	public void testChangePasswordWithIncorrectPassword()
			throws InstanceNotFoundException, IncorrectPasswordException {
		LoginResult loginResult = userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);

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
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		LoginResult newResult = 
			userService.changePrivileges(loginResult.getUserProfileId(),	
					Privileges_TYPES.NONE);
		assertEquals(newResult.getPrivileges(),Privileges_TYPES.NONE);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsAdmin() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsCompetitor() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsVoter() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		userService.changePrivileges(loginResult.getUserProfileId(),	
				Privileges_TYPES.ADMIN);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsNone() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
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
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsCompetitor_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.COMPETITOR);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsVoter_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.VOTER);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void testChangePrivilegesToAdminAsNone_2() throws InstanceNotFoundException, 
			IncorrectPasswordException, InsufficientPrivilegesException{
		LoginResult loginResult = 
				userService.login(UserProfileDbUtil.getTestUserProfile()
				.getLoginName(), UserProfileDbUtil.getTestClearPassword(), false);
		loginResult = userService.changePrivileges(loginResult.getUserProfileId(), 
				Privileges_TYPES.NONE);
		userService.changePrivilegesToAdmin(loginResult.getUserProfileId(), 
				loginResult.getUserProfileId());
	}

}
