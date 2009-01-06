package es.udc.acarballal.elmas.web.util;

import org.apache.tapestry5.services.Cookies;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;

public class CookiesManager {

	private static final String LOGIN_NAME_COOKIE = "loginName";
	private static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";
	private static final String PRIVILEGES_COOKIE = "privileges";
    private static final int REMEMBER_MY_PASSWORD_AGE =
        30 * 24 * 3600; // 30 days in seconds

	public static void leaveCookies(Cookies cookies, String loginName,
			String encryptedPassword, String privileges) {
		cookies.writeCookieValue(LOGIN_NAME_COOKIE, loginName, 
			REMEMBER_MY_PASSWORD_AGE);
		cookies.writeCookieValue(ENCRYPTED_PASSWORD_COOKIE, encryptedPassword,
			REMEMBER_MY_PASSWORD_AGE);
		cookies.writeCookieValue(PRIVILEGES_COOKIE, privileges);
	}
	
	public static void removeCookies(Cookies cookies) {
		cookies.removeCookieValue(LOGIN_NAME_COOKIE);
		cookies.removeCookieValue(ENCRYPTED_PASSWORD_COOKIE);
		cookies.readCookieValue(PRIVILEGES_COOKIE);
	}

	public static String getLoginName(Cookies cookies) {
		return cookies.readCookieValue(LOGIN_NAME_COOKIE);
	}

	public static String getEncryptedPassword(Cookies cookies) {
		return cookies.readCookieValue(ENCRYPTED_PASSWORD_COOKIE);
	}
	
	public static Privileges_TYPES getPrivileges(Cookies cookies){
		String p = cookies.readCookieValue(PRIVILEGES_COOKIE);
		if(p.equals("admin")) return Privileges_TYPES.ADMIN;
		if(p.equals("voter")) return Privileges_TYPES.VOTER;
		if(p.equals("competitor")) return Privileges_TYPES.COMPETITOR;
		return Privileges_TYPES.NONE;
	}

}
