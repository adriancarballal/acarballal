package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ComplaintUserComment {

	private Long complaintedUserComment;
	
	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private UserService userService;
	
	Object onActivate(Long userCommentId){
		this.complaintedUserComment = userCommentId;
		try {
			userService.complaintUserComment(complaintedUserComment, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Index.class;
	}
}
