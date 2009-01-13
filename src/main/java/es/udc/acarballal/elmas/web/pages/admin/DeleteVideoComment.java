package es.udc.acarballal.elmas.web.pages.admin;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class DeleteVideoComment {

	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private VideoService videoService;
	
	Object onActivate(Long commentId){
		try {
			videoService.deleteVideoComment(commentId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			//System.out.println("--- Error 1 ---");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			//System.out.println("--- Error 2 ---");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ShowVideoCommentComplaint.class;
	}
}
