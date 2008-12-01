package es.udc.acarballal.elmas.test.model.util;

import java.util.Calendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.util.PasswordEncrypter;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.video.VideoDao;
import es.udc.acarballal.elmas.test.util.GlobalNames;

/**
 * This utility class allows to initialize the database with an initial state
 * for testing all model services. It illustrates a good approach when there 
 * exist several model services that need a common initial database state for 
 * testing (which is the usual case). Internally, the class create and remove 
 * entity objects by using DAOs directly.
 * <p>
 * The expected usage of this class is as follows:
 * <ul>
 * <li>Call <code>VideoDbUtil.populateDb</code> from a <code>@BeforeClass</code> 
 * method.</li>
 * <li>Call <code>VideoDbUtil.cleanDb</code> from a <code>@AfterClass</code> method.
 * </li>
 * </ul>
 * Note that calling these methods from <code>@Before</code> and
 * <code>@After</code> methods causes many queries to be launched to the
 * database for each test method.
 */
public class VideoDbUtil {
	
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				GlobalNames.SPRING_CONFIG_FILE_LOCATION);

		transactionManager = (PlatformTransactionManager) context
				.getBean("transactionManager");
		userProfileDao = (UserProfileDao) context.getBean("userProfileDao");
		videoDao = (VideoDao) context.getBean("videoDao");
	}
	
	private static UserProfile testUserProfile;
	private static Video testVideo;
	private static String testClearPassword = "user1ClearPassword";

	private static UserProfileDao userProfileDao;
	private static VideoDao videoDao;
	private static PlatformTransactionManager transactionManager;
	
	public static UserProfile getTestUserProfile() {
		return testUserProfile;
	}
	
	public static Video getTestVideo(){
		return testVideo;
	}

	public static void populateDb() {
		/*
		 * Since this method is supposed to be called form a @BeforeClass 
		 * method, it works directly with "TransactionManager", since 
		 * @BeforeClass methods with Spring TestContext do not run in the 
		 * context of a transaction (which is required for DAOs to work).
		 */

		TransactionStatus transactionStatus = transactionManager
				.getTransaction(null);

		testUserProfile = new UserProfile("user1", PasswordEncrypter
				.crypt(testClearPassword), "name1", "lastName1", "user1@udc.es");
		testUserProfile.setPrivileges(Privileges_TYPES.ADMIN);
		
		try {
			userProfileDao.create(testUserProfile);
			testVideo = new Video(testUserProfile, "VideoExample", "CommentExample", 
					"-home-data-snapshots-001.jpg", Calendar.getInstance());
			videoDao.create(testVideo);
			
		} catch (Throwable e) {
			transactionManager.rollback(transactionStatus);
		}

		transactionManager.commit(transactionStatus);

	}

	public static void cleanDb() throws Exception {
		/*
		 * For the same reason as "cleanDb" (with regard to @AfterClass 
		 * methods), this method works directly with "TransactionManager".
		 */

		TransactionStatus transactionStatus = transactionManager
				.getTransaction(null);
		
		try {

			videoDao.remove(testVideo.getVideoId());
			userProfileDao.remove(testUserProfile.getUserProfileId());
			testUserProfile = null;
			testVideo = null;
			
		} catch (Throwable e) {
			transactionManager.rollback(transactionStatus);
		}

		transactionManager.commit(transactionStatus);
	}

}