package es.udc.acarballal.elmas.web.components;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
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
	@Persist
	private List<Video> best;

	@InjectPage
	private FindVideos findVideos;

	@SuppressWarnings("unused")
	@Component
	private Form findVideosForm;

	@SuppressWarnings("unused")
	@Property
	private String keys;

	@SuppressWarnings("unused")
	@Component(id = "keys")
	private TextField keysField;

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

	private Video video;

	@Inject
	private VideoService videoService;

	public List<Video> getBest() {
		return best;
	}

	public boolean getIsAdmin() {
		if (userSessionExists
				&& userSession.getPrivileges() == Privileges_TYPES.ADMIN)
			return true;
		return false;
	}

	public Video getVideo() {
		return video;
	}

	@BeginRender
	void insertBest() {
		best = videoService.findMostVoted(BEST_TOTAL);
	}

	Object onSuccess() {

		findVideos.setKeys(keys);
		return findVideos;
	}

	public void setBest(List<Video> best) {
		this.best = best;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
	
	
	// FECHA
	@Inject
	private Locale locale;
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public Calendar getToday(){
		return Calendar.getInstance();
	}
}