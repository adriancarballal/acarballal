package es.udc.acarballal.elmas.model.videoservice;

import java.util.List;

import es.udc.acarballal.elmas.model.video.Video;

public class VideoBlock {

    private List<Video> videos;
    private boolean existMoreVideos;

    public VideoBlock(List<Video> videos, boolean existMoreVideos) {
        
    	///////////////////////////////////////////////
    	System.out.println("SIZE: " + videos.size());
        this.videos = videos;
        this.existMoreVideos = existMoreVideos;

    }

	public List<Video> getVideos() {
		return videos;
	}

	public boolean getExistMoreVideos() {
		return existMoreVideos;
	}
    
}
