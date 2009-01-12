package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ComplaintVideoComment {

	private Long complaintedVideoComment;
	
	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private VideoService videoService;
	
	Object onActivate(Long videoCommentId){
		this.complaintedVideoComment = videoCommentId;
		try {
			videoService.complaintOfVideoComment(complaintedVideoComment, userSession.getUserProfileId());
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
