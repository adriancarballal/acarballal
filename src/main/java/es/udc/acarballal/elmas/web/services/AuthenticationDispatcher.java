package es.udc.acarballal.elmas.web.services;

import java.io.IOException;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;

import es.udc.acarballal.elmas.web.util.PageSession;
import es.udc.acarballal.elmas.web.util.UserSession;

public class AuthenticationDispatcher implements Dispatcher {
	
	
	private final static String INIT_PAGE = "/";
	private final static String LOGIN_PAGE = "/user/login";
	private final static String PARTICIPATE_PAGE = "/user/updateprofile";
	
	private ApplicationStateManager applicationStateManager;
	private ComponentClassResolver componentClassResolver;
	private ComponentSource componentSource;

	public AuthenticationDispatcher(ApplicationStateManager applicationStateManager,
		ComponentClassResolver componentClassResolver,
		ComponentSource componentSource) {
		
		this.applicationStateManager = applicationStateManager;
		this.componentClassResolver = componentClassResolver;
		this.componentSource = componentSource;
		
	}

	public boolean dispatch(Request request, Response response)
		throws IOException {
		
		Component page = componentSource.getPage(getPageName(request));
		AuthenticationPolicy policy =
			page.getClass().getAnnotation(AuthenticationPolicy.class);
		
		if (policy == null) {
			return false;
		}
		
		AuthenticationPolicyType policyType = policy.value();
		boolean userAuthenticated = 
			applicationStateManager.exists(UserSession.class);
		
		
		
		boolean isAdministrator = false;
		boolean isParticipating = false;
			
		try{
			UserSession userSession = 
				applicationStateManager.getIfExists(UserSession.class);
			if(userSession.getPrivileges()==Privileges_TYPES.ADMIN){
				isParticipating = true;
				isAdministrator = true;
			}
			if(userSession.getPrivileges()==Privileges_TYPES.COMPETITOR)
				isParticipating = true;
		}
		catch (Exception e){}			
			
		
		switch (policyType) {
		
		case AUTHENTICATED_USERS:

			if (!userAuthenticated) {
				
				applicationStateManager.set(PageSession.class, new PageSession(page.getClass()));
				response.sendRedirect(request.getContextPath() +
					LOGIN_PAGE);
				return true; // Leave the chain.
			}
			break;
			
		case NON_AUTHENTICATED_USERS:
			
			if (userAuthenticated) {
				response.sendRedirect(request.getContextPath() + 
					INIT_PAGE);
				return true; // Leave the chain.
			}
			break;
			
		case ADMINISTRATORS:
			
			if (!isAdministrator) {
				response.sendRedirect(request.getContextPath() + 
					INIT_PAGE);
				return true; // Leave the chain.
			}
			break;			
			
		case PARTICIPANTS:
			if (!userAuthenticated) {
				applicationStateManager.set(PageSession.class, new PageSession(page.getClass()));
				response.sendRedirect(request.getContextPath() + 
					LOGIN_PAGE);
				return true; // Leave the chain.
			}
			if (!isParticipating) {
				applicationStateManager.set(PageSession.class, new PageSession(page.getClass()));
				response.sendRedirect(request.getContextPath() + 
						PARTICIPATE_PAGE);
					return true; // Leave the chain.
			}
			break;			

		default:
			break;
		
		}
		
		return false;
		
	}
	
	private String getPageName(Request request) {
		
		String path = request.getPath();

		/* 
		 * Remove leading slash.
		 * 
		 * TAPESTRY-1343: Sometimes path is the empty string (it should always 
		 * be at least a slash, but Tomcat may return the empty string for a 
		 * root context request).
		 */
		path = path.length() == 0 ? path : path.substring(1);
		
		/* Ignore trailing slashes in the path. */
		while (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		/* Remove activation context (if exists). */
		int nextSlash;
		
		do {
			
			if (componentClassResolver.isPageName(path)) {
				return path;
			}
			
			nextSlash = path.lastIndexOf('/', path.length() - 1);
			
			if (nextSlash > 0) {
				path = path.substring(0, nextSlash);
			}
			
		} while (nextSlash > 0);
		
		return "";
		
	}

}
