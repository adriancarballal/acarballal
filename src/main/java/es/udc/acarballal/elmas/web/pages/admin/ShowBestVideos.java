package es.udc.acarballal.elmas.web.pages.admin;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.admin.Reports.SHOW_BEST_TYPE;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINISTRATORS)
public class ShowBestVideos {
	
	private int count = 0;
	
	@Inject
	private Locale locale;
	
	private SHOW_BEST_TYPE type;
	@SuppressWarnings("unused")
	@Property
	private Video video;
	
	@SuppressWarnings("unused")
	@Property
	private List<Video> videos;
	
	@Inject
	private VideoService videoService;

	public int getCount() {
		return count;
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public SHOW_BEST_TYPE getType() {
		return type;
	}

	void onActivate(SHOW_BEST_TYPE type, int count) {
		this.type = type;
		this.count = count;
		
		if(this.type==SHOW_BEST_TYPE.ALL){
			videos=videoService.findMostVoted(count);
		}
		else{
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			startDate.set(Calendar.HOUR, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.AM_PM, Calendar.AM);
			startDate.set(Calendar.MILLISECOND, 0);
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			endDate.set(Calendar.HOUR, 23);
			endDate.set(Calendar.MINUTE, 59);
			endDate.set(Calendar.SECOND, 59);
			endDate.set(Calendar.AM_PM, Calendar.PM);
			endDate.set(Calendar.MILLISECOND, 999);
			videos=videoService.findMostVoted(startDate, endDate, count);
		}
	}

	Object[] onPassivate() {
		return new Object[] {type, count};
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public void setType(SHOW_BEST_TYPE type) {
		this.type = type;
	}
}
