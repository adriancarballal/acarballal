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

public class ShowBestVideos {
	
	@SuppressWarnings("unused")
	@Property
	private List<Video> videos;
	
	@SuppressWarnings("unused")
	@Property
	private Video video;
	
	private SHOW_BEST_TYPE type;
	private int count = 10;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;

	public SHOW_BEST_TYPE getType() {
		return type;
	}

	public void setType(SHOW_BEST_TYPE type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	Object[] onPassivate() {
		return new Object[] {type, count};
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

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
}
