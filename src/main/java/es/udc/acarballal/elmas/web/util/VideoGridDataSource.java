package es.udc.acarballal.elmas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class VideoGridDataSource implements GridDataSource {
	
	private VideoService videoService;
	private Long userId;
	private List<Video> videos;
	private int totalNumberOfVideos;
	private int startIndex;
	private boolean videoNotFound;
	
	public VideoGridDataSource(VideoService videoService, Long userId) {
		
		this.videoService = videoService;
		this.userId = userId;
	
		totalNumberOfVideos = videoService.getNumberOfVideosByUser(userId);
		
		if (totalNumberOfVideos == 0) videoNotFound = true;
		
	}
	
    public int getAvailableRows() {
        return totalNumberOfVideos;
    }

    public Class<Video> getRowType() {
        return Video.class;
    }

    public Object getRowValue(int index) {
        return videos.get(index-this.startIndex);
    }

    public void prepare(int startIndex, int endIndex,
    	List<SortConstraint> sortConstraints) {
    	
       	videos = videoService.findVideosByUser(userId, startIndex, 
       				endIndex-startIndex+1).getVideos();
        this.startIndex = startIndex;
		
    }
    
    public boolean getVideoNotFound() {
    	return videoNotFound;
    }
    
    

}
