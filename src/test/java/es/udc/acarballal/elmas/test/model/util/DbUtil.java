package es.udc.acarballal.elmas.test.model.util;

import java.util.Calendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.usercomment.UserCommentDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.util.PasswordEncrypter;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.video.VideoDao;
import es.udc.acarballal.elmas.model.videocomment.VideoComment;
import es.udc.acarballal.elmas.model.videocomment.VideoCommentDao;
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
public class DbUtil {
	
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				GlobalNames.SPRING_CONFIG_FILE_LOCATION);

		transactionManager = (PlatformTransactionManager) context
				.getBean("transactionManager");
		userProfileDao = (UserProfileDao) context.getBean("userProfileDao");
		videoDao = (VideoDao) context.getBean("videoDao");
		userCommentDao = (UserCommentDao) context.getBean("userCommentDao");
		videoCommentDao = (VideoCommentDao) context.getBean("videoCommentDao");
	}
	
	private static UserProfile testUserProfile;
	private static UserProfile commentatorProfile;
	private static String testClearPassword = "admin";
	//private static String testClearPassword = "user1ClearPassword";
	private static UserProfileDao userProfileDao;
	
	private static Video testVideo;
	private static Video testVideoMultiKey;
	private static VideoDao videoDao;
	
	private static UserCommentDao userCommentDao;
	private static UserComment userComment1;
	private static UserComment userComment2;
	private static UserComment userComment3;
	
	private static VideoCommentDao videoCommentDao;
	private static VideoComment videoComment;
	
	private static PlatformTransactionManager transactionManager;
	
	public static UserProfile getTestUserProfile() {
		return testUserProfile;
	}
	
	public static UserProfile getCommentatorProfile(){
		return commentatorProfile;
	}
	
	public static String getTestClearPassword() {
		return testClearPassword;
	}
	
	public static Video getTestVideo(){
		return testVideo;
	}
	
	public static Video getTestVideoMultiKey(){
		return testVideoMultiKey;
	}
	
	public static UserComment getUserComment1() {
		return userComment1;
	}

	public static UserComment getUserComment2() {
		return userComment2;
	}

	public static UserComment getUserComment3() {
		return userComment3;
	}
	
	public static VideoComment getVideoComment() {
		return videoComment;
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
		
		commentatorProfile = new UserProfile("adrian_voter", PasswordEncrypter
				.crypt(testClearPassword), "commentator", "commentator", 
				"commentator@udc.es");
		commentatorProfile.setPrivileges(Privileges_TYPES.VOTER);
		
		try {
			userProfileDao.create(testUserProfile);
			userProfileDao.create(commentatorProfile);
			testVideo = new Video(testUserProfile, "Example Video file", 
					"CommentExample", "-home-data-snapshots-001.jpg", 
					"video.vob", "video.flv", "video.mp4", Calendar.getInstance());
			videoDao.create(testVideo);
			testVideoMultiKey = new Video(testUserProfile, "Video n2 example", 
					"CommentExample", "-home-data-snapshots-001.jpg", 
					"video.vob", "video.flv", "video.mp4", Calendar.getInstance());
			videoDao.create(testVideoMultiKey);
			testVideo = new Video(testUserProfile, "VideoExample", 
					"CommentExample", "-home-data-snapshots-001.jpg", 
					"video.vob", "video.flv", "video.mp4", Calendar.getInstance());
			videoDao.create(testVideo);
			userComment1 = new UserComment(testUserProfile, commentatorProfile,  
					"He is a good administrator", Calendar.getInstance());
			userCommentDao.create(userComment1);
			userComment2 = new UserComment(commentatorProfile, testUserProfile,    
					"He has been warnned", Calendar.getInstance());
			userCommentDao.create(userComment2);
			userComment3 = new UserComment(commentatorProfile, testUserProfile,    
					"He has been warnned twice", Calendar.getInstance());
			userCommentDao.create(userComment3);
			videoComment = new VideoComment(testVideo, commentatorProfile, 
					"Could be better...", Calendar.getInstance());
			videoCommentDao.create(videoComment);
			
			
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

			//videoDao.remove(testVideo.getVideoId());
			userProfileDao.remove(commentatorProfile.getUserProfileId());
			userProfileDao.remove(testUserProfile.getUserProfileId());
			testUserProfile = null;
			commentatorProfile = null;
			testVideo = null;
			
		} catch (Throwable e) {
			transactionManager.rollback(transactionStatus);
		}

		transactionManager.commit(transactionStatus);
	}

}
