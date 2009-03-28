package es.udc.acarballal.elmas.test.model.encoderservice;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.encoderservice.EncoderService;
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.LoginResult;
import es.udc.acarballal.elmas.model.userservice.MessageBlock;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.test.model.util.DbUtil;
import es.udc.acarballal.elmas.test.util.GlobalNames;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { GlobalNames.SPRING_CONFIG_FILE_LOCATION })
@Transactional
public class EncoderServiceTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	
	private UserService userService;
	private EncoderService encoderService;

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
	public void setEncoderService(EncoderService encoderService) {
		this.encoderService = encoderService;
	}
	
	@Test
	public void sendConfirmationMessage() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		encoderService.sendConfirmationMessage(admin.getUserProfileId(), 
				"message");
		
		MessageBlock block = 
			userService.findUserInBox(admin.getUserProfileId(), 0, 1);
		
		assertEquals(block.getMessages().size(), 1);
		assertEquals(block.getMessages().get(0).getReceiver().getUserProfileId(),
				admin.getUserProfileId());
		assertEquals(block.getMessages().get(0).getText(), "message");
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void sendConfirmationMessageNoReceiver() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		encoderService.sendConfirmationMessage(NON_EXISTENT_USER_PROFILE_ID, 
				"message");
	}	
	
	@Test
	public void sendErrorMessage() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		LoginResult admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false);
		
		encoderService.sendErrorMessage(admin.getUserProfileId(), 
				"message");
		
		MessageBlock block = 
			userService.findUserInBox(admin.getUserProfileId(), 0, 1);
		
		assertEquals(block.getMessages().size(), 1);
		assertEquals(block.getMessages().get(0).getReceiver().getUserProfileId(),
				admin.getUserProfileId());
		assertEquals(block.getMessages().get(0).getText(), "message");
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void sendErrorMessageNoReceiver() 
			throws InstanceNotFoundException, IncorrectPasswordException{
		
		encoderService.sendErrorMessage(NON_EXISTENT_USER_PROFILE_ID, 
				"message");
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void encodeVideoAsNone() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		
		admin = userService.changePrivileges(admin, 
				Privileges_TYPES.NONE).getUserProfileId();
		encoderService.encodeVideo(null, admin, null, null);
	}
	
	@Test(expected = InsufficientPrivilegesException.class)
	public void encodeVideoAsVoter() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		Long admin = 
			userService.login(DbUtil.getTestUserProfile().getLoginName(), 
					DbUtil.getTestClearPassword(), false).getUserProfileId();
		
		admin = userService.changePrivileges(admin, 
				Privileges_TYPES.VOTER).getUserProfileId();
		encoderService.encodeVideo(null, admin, null, null);
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void encodeVideoNoneUser() 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException{
		
		encoderService.encodeVideo(null, 
				NON_EXISTENT_USER_PROFILE_ID, null, null);
	}

}
