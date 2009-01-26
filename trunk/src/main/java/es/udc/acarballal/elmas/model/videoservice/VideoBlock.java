package es.udc.acarballal.elmas.model.videoservice;

import java.util.List;

import es.udc.acarballal.elmas.model.video.Video;

public class VideoBlock {

    private boolean existMoreVideos;
    private List<Video> videos;

    public VideoBlock(List<Video> videos, boolean existMoreVideos) {
        
        this.videos = videos;
        this.existMoreVideos = existMoreVideos;

    }

	public boolean getExistMoreVideos() {
		return existMoreVideos;
	}

	public List<Video> getVideos() {
		return videos;
	}
    
}
