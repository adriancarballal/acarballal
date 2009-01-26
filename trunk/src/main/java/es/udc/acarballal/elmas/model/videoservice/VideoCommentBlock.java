package es.udc.acarballal.elmas.model.videoservice;

import java.util.List;

import es.udc.acarballal.elmas.model.videocomment.VideoComment;

public class VideoCommentBlock {
	private boolean existMoreVideoComments;
	private List<VideoComment> videoComments;
	
	public VideoCommentBlock(List<VideoComment> videoComments, boolean existMoreVideoComments){
		this.videoComments = videoComments;
		this.existMoreVideoComments = existMoreVideoComments;
	}

	public boolean getExistMoreUserComments() {
		return existMoreVideoComments;
	}

	public List<VideoComment> getUserComments() {
		return videoComments;
	}
}
