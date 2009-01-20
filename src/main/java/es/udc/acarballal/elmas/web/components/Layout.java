package es.udc.acarballal.elmas.web.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.FindVideos;
import es.udc.acarballal.elmas.web.util.UserSession;

public class Layout {
	
	private final static int BEST_TOTAL = 3;
	private Video video;
	
	@Persist
	private List<Video> best = new ArrayList<Video>();
		
	@Inject
	private VideoService videoService;
	
	@Inject
	@Path("context:/logo/logo.jpg")
	@Property
	private Asset flag;
	public Asset getFlag()	{
		return flag;
	}
	
	// TODO esto no se prodrá quitar???
	@SuppressWarnings("unused")
	@Property
	@Parameter(required = false, defaultPrefix = "literal")
	private String menuExplanation;
	
	@SuppressWarnings("unused")
	@Property
	@Parameter(required = true, defaultPrefix = "literal")
	private String pageTitle;

	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@SuppressWarnings("unused")
	@Component
	private Form findVideosForm;
	
	@SuppressWarnings("unused")
	@Property
	private String keys;
	
	@SuppressWarnings("unused")
	@Component(id = "keys")
	private TextField keysField;
	
	@InjectPage
	private FindVideos findVideos;
	
	@BeginRender
	void insertBest(){
		best = videoService.findMostVoted(BEST_TOTAL);
	}
	
	public List<Video> getBest() {
		return best;
	}

	public void setBest(List<Video> best) {
		this.best = best;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	Object onSuccess(){
                 
		findVideos.setKeys(keys);
        return findVideos;
    }
	
	public boolean getIsAdmin(){
		if (userSessionExists && 
				userSession.getPrivileges() == Privileges_TYPES.ADMIN)
			return true;
		return false;
	}
	
}