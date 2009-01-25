package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.videocomment.VideoComment;
import es.udc.acarballal.elmas.model.videoservice.VideoCommentBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;

public class MyVideoComments {

	private int startIndex = 0;
	private int count = 4;
	private VideoCommentBlock videoCommentBlock;
	
	@SuppressWarnings("unused")
	@Property
	private VideoComment videoComment;
	
	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;
	
	public List<VideoComment> getVideoComments(){
		return videoCommentBlock.getUserComments();
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (videoCommentBlock.getExistMoreUserComments()) {
			return new Object[] {startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex, count};
	}
	
	void onActivate(int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		videoCommentBlock = 
			videoService.findVideoCommentsByUserId(
					userSession.getUserProfileId(), startIndex, count);
			
	}
}
